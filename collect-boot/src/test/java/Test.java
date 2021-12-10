import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayEcoActivityRecycleSendRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.*;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.tzj.collect.Application;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.excel.ExcelData;
import com.tzj.collect.common.excel.ExcelUtils;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.handler.OrderCompleteHandler;
import com.tzj.collect.core.handler.OrderHandler;
import com.tzj.collect.core.param.mysl.MyslBean;
import com.tzj.collect.core.param.mysl.MyslItemBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.util.EhCache2Utils;
import lombok.SneakyThrows;
import net.sf.ehcache.CacheManager;
import org.aspectj.weaver.ast.Or;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/5/6 14:47
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class Test {


    @Autowired
    PaymentService paymentService;
    @Resource(name = "certAlipayClient")
    AlipayClient certAlipayClient;
    @Autowired
    OrderService orderService;
    @Autowired
    AnsycMyslService ansycMyslService;
    @Autowired
    IotOrderDetailService iotOrderDetailService;

    @Autowired
    OrderHandler orderHandler;


    @Autowired
    PointListService pointListService;

    @Autowired
    PointService pointService;


    /**
     * 清除过期积分
     */
    @SneakyThrows
    @org.junit.Test
    public void cleanOverduePoint() {

        //截止时间
        String endDate = "2020-03-31 23:59:59";
        //获取期间增加积分信息
        List<Map<String, Object>> addPointInfoEndWithCreateDate = pointListService.getAddPointInfoEndWithCreateDate(endDate);
        List<Map<String, Object>> reducePointInfoEndWithCreateDate = pointListService.getReducePointInfoEndWithCreateDate(endDate);


        //转map
        Map<String, Double> addPointMap = new HashMap();
        addPointInfoEndWithCreateDate.forEach(map -> {
            addPointMap.put(map.get("aliUserId") + "", (Double) map.get("points"));
        });


        Map<String, Double> reduceMap = new HashMap();
        reducePointInfoEndWithCreateDate.forEach(map -> {
            reduceMap.put(map.get("aliUserId") + "", (Double) map.get("points"));
        });

        ExcelData excelData = new ExcelData();
        excelData.setName("逾期积分清除明细");
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("aliUserId");
        titles.add("历史剩余积分");
        titles.add("历史总积分");
        titles.add("当前剩余积分");
        titles.add("当前总积分");
        titles.add("扣除积分");
        excelData.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        final List<Object>[] row = new List[]{null};

        System.out.println("总数据-----------------:" + addPointMap.size());
        final int[] i = {1};
        //计算需要扣分用户
        addPointMap.keySet().forEach(aliUserId -> {

            try {
                System.out.println("当前正在执行-----------------:" + i[0]);
                double point = 0;

                double addPoint = addPointMap.get(aliUserId);

                //判断是否积分已经使用

                Double reducePoint = reduceMap.get(aliUserId);

                if (reducePoint != null) {

                    //判断是否超过增加的积分，超过增加的积分就清除了
                    if (addPoint > reducePoint) {
                        point = addPoint - reducePoint;
                    }
                } else {
                    point = addPoint;
                }

                //扣除积分大于0  直接扣除积分
                if (point != 0) {
                    Point pointinfo = pointService.getPoint(aliUserId);
                    row[0] = new ArrayList();
                    row[0].add(aliUserId);
                    row[0].add(pointinfo.getRemainPoint());
                    row[0].add(pointinfo.getPoint());

                    //判断用户积分够不够扣，直接扣完
                    if (point > pointinfo.getRemainPoint()) {
                        point = pointinfo.getRemainPoint();
                    }
                    if (point != 0) {
                        pointinfo.setPoint(pointinfo.getPoint() - point);
                        pointinfo.setRemainPoint(pointinfo.getRemainPoint() - point);
                        row[0].add(pointinfo.getRemainPoint());
                        row[0].add(pointinfo.getPoint());
                        row[0].add(point);
                        rows.add(row[0]);
                        pointService.updateById(pointinfo);
                    }

                }
                i[0]++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        OutputStream outputStream = new FileOutputStream("/Users/xiangzhongguo/Downloads/clean-2020-03-31.xlsx");
        ExcelUtils.exportExcel(excelData, outputStream);

        System.out.println("执行完成");
    }


    @SneakyThrows
    @org.junit.Test
    public void creatIotOrderByMqtt() {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        String sn = "test12345678901";
        String subject = "垃圾分类回收订单(收呗):" + sn;

        model.setBody(subject);
        model.setSubject(subject);
        model.setOutTradeNo("test123456789011");
        model.setTimeoutExpress("2m");
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2017011905224137");
        model.setExtendParams(extendParams);
        model.setTotalAmount("0.11");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl("www.baidu.com");

        //这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = certAlipayClient.sdkExecute(request);


        System.out.println(JSONObject.toJSON(response).toString());
    }


    /**
     * 转账
     *
     * @return
     * @throws AlipayApiException
     */
    @org.junit.Test
    public void aliPayTransfer() throws AlipayApiException {
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo("trans_tzj_20211013001");
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity("2088302884857519");
        payeeInfo.setIdentityType("ALIPAY_USER_ID");
        model.setTransAmount("8.2");
        model.setOrderTitle("垃圾分类回收(收呗)货款");
        model.setRemark("垃圾分类回收(收呗)货款");
        model.setPayeeInfo(payeeInfo);
        model.setBizScene("DIRECT_TRANSFER");
        request.setBizModel(model);
        System.out.println("转账结果：" + JSONObject.toJSON(certAlipayClient.certificateExecute(request)).toString());

    }

    @SneakyThrows
    @org.junit.Test
    public void transferQuery() {
        AlipayFundTransOrderQueryResponse response = paymentService.getTransfer("test1234567890");
        System.out.println(JSONObject.toJSON(response).toString());
    }

    @Autowired
    MemberService memberService;


    @SneakyThrows
    @org.junit.Test
    public void mysl() {
        List<Order> orders = orderService.getOrders();
        System.out.println("共获取到订单数量：" + orders.size());

        orders.forEach(order -> {
            if ("1".equals(order.getIsMysl()) || order.getIsScan().equals("1")) {
                //给用户增加蚂蚁能量
                orderService.myslOrderData(order.getId().toString());
            }
        });

        orders.forEach(order -> {
            System.out.println(order.getOrderNo());
        });
        System.out.println("能量发完");
    }
}
