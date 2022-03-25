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
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.tzj.collect.Application;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.excel.ExcelData;
import com.tzj.collect.common.excel.ExcelUtils;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.common.utils.ExcelHelper;
import com.tzj.collect.core.handler.OrderCompleteHandler;
import com.tzj.collect.core.handler.OrderHandler;
import com.tzj.collect.core.mapper.AreaMapper;
import com.tzj.collect.core.param.mysl.MyslBean;
import com.tzj.collect.core.param.mysl.MyslItemBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.util.CopyUtil;
import com.tzj.module.easyopen.util.EhCache2Utils;
import lombok.SneakyThrows;
import net.sf.ehcache.CacheManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.aspectj.weaver.ast.Or;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @Autowired
    VoucherAliService voucherAliService;

    @SneakyThrows
    @org.junit.Test
    public void makeCode() {
        voucherAliService.makeCode("289");
    }


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
        model.setOutBizNo("trans_tzj_20220221001");
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity("2088332777855261");
        payeeInfo.setIdentityType("ALIPAY_USER_ID");
        model.setTransAmount("15");
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
    @Autowired
    MyslRequestLogService myslRequestLogService;


    @SneakyThrows
    @org.junit.Test
    public void mysl() {
        List<MyslRequestLog> orders = myslRequestLogService.getNoMysqlList();
        System.out.println("共获取到订单数量：" + orders.size());

        orders.forEach(order -> {
            ansycMyslService.updateForest(order);
        });
        System.out.println("能量发完");
    }


    @Autowired
    AreaService areaService;

    @Autowired
    CompanyStreetElectroMobileService companyStreetElectroMobileService;


    @SneakyThrows
    @org.junit.Test
    public void diandongche() {
        List<Map<String, Object>> excelObj =
                ExcelHelper.importExeclFileForPoi(new File("/Users/xiangzhongguo/Downloads/电瓶车可覆盖区域.xlsx"), 1, "xlsx");
        Integer companyId = 277;

        System.out.println("共" + excelObj.size());
        List<CompanyStreetElectroMobile> companyStreetElectroMobiles = new ArrayList<>();
        int i = 1;
        for (Map excel : excelObj) {
            try {
                System.out.println("正在执行第" + i);
                String streetName = excel.get("map1").toString();
                String districtName = excel.get("map0").toString();
                List<Area> byAreaName = areaService.findByAreaName(streetName);

                for (Area area : byAreaName) {
                    Area district = areaService.selectById(area.getParentId());
                    if(district !=null && district.getAreaName().equals(districtName)){
                        CompanyStreetElectroMobile companyStreetElectroMobile = new CompanyStreetElectroMobile();
                        companyStreetElectroMobile.setCompanyId(companyId);
                        companyStreetElectroMobile.setAreaId(area.getParentId());
                        companyStreetElectroMobile.setStreetId(Integer.parseInt(area.getId().toString()));
                        companyStreetElectroMobiles.add(companyStreetElectroMobile);

                        if (companyStreetElectroMobiles.size() >= 200 || i == excelObj.size()) {
                            companyStreetElectroMobileService.saveList(companyStreetElectroMobiles);
                            companyStreetElectroMobiles.clear();
                        }
                        break;
                }
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        System.out.println("结束");
    }

    @Autowired
    CompanyCategoryCityNameService companyCategoryCityNameService;

    @Autowired
    CategoryService categoryService;

    @org.junit.Test
    public void send() {


        List<String> categoryId = new ArrayList<>();
        categoryId.add("177");
        categoryId.add("178");
        categoryId.add("179");
        categoryId.add("180");
        categoryId.add("181");
        String companyId = "277";
        List<Map<String, Object>> cityId = companyStreetElectroMobileService.getCityId();
        for (Map<String, Object> map : cityId) {
            for (String s : categoryId) {
                CompanyCategoryCityName companyCategoryCityName = companyCategoryCityNameService.selectOne(new EntityWrapper<CompanyCategoryCityName>()
                        .eq("company_id", companyId).eq("city_id", map.get("id").toString()).eq("category_id", s));
                if (null == companyCategoryCityName) {
                    companyCategoryCityName = new CompanyCategoryCityName();
                    Category category = categoryService.selectById(s);
                    Category parentCategory = categoryService.selectById(category.getParentId());
                    companyCategoryCityName.setCompanyId(companyId);
                    companyCategoryCityName.setCityId(map.get("id").toString());
                    companyCategoryCityName.setCategoryId(category.getId().intValue());
                    companyCategoryCityName.setParentId(category.getParentId());
                    companyCategoryCityName.setParentName(parentCategory.getName());
                    companyCategoryCityName.setParentIds(category.getParentIds());
                    companyCategoryCityName.setPrice(category.getMarketPrice());
                    companyCategoryCityName.setUnit(category.getUnit());
                    companyCategoryCityNameService.insert(companyCategoryCityName);
                }
            }

        }
        System.out.println("执行完成");

    }


    @Autowired
    RecyclersService recyclersService;
    @Autowired
    CompanyRecyclerService companyRecyclerService;


    @SneakyThrows
    @org.junit.Test
    public void insertRecycler() {
        List<Map<String, Object>> excelObj =
                ExcelHelper.importExeclFileForPoi(new File("/Users/xiangzhongguo/Downloads/号码.xlsx"), 0, "xlsx");
        Integer companyId = 279;

        for (Map excel : excelObj) {

            Recyclers recyclers1=new Recyclers();
            recyclers1.setStatus("0");
            recyclers1.setAuthStatus("0");
            recyclers1.setTel(excel.get("map0").toString());
            recyclers1.setPassword("111111");
            recyclers1.setDsddRecycler(1);
            recyclersService.insert(recyclers1);


            CompanyRecycler companyRecyclers1=new CompanyRecycler();
            companyRecyclers1.setRecyclerId(Integer.parseInt(recyclers1.getId()+""));
            companyRecyclers1.setStatus("1");
            companyRecyclers1.setCompanyId(companyId);
            companyRecyclers1.setType("1");
            companyRecyclers1.setIsManager("1");

            companyRecyclerService.insert(companyRecyclers1);


        }

        System.out.println("结束");
    }


    @Autowired
    AdminService adminService;

    @org.junit.Test
    public void insertRecycler1() {
        Admin ad =new Admin();
        ad.setName("111");
        ad.setPassword("2222");
        ad.setUsername("333");
        adminService.insert(ad);
    }



    @Autowired
    AreaMapper areaMapper;


    @org.junit.Test
    public void getRange(){

        StringBuffer  code =new StringBuffer();

        List<String> areaRange = areaMapper.getAreaRange();

        List<String> areaRangeCopy =  new ArrayList<>();
        CollectionUtils.addAll(areaRangeCopy, new Object[areaRange.size()]);
        Collections.copy(areaRangeCopy, areaRange);

            List<Area> cityRange = areaMapper.getCityRange();

        for (Area city : cityRange) {
            List<String> areas = areaMapper.selectByParentId(city.getId());
            if(areaRange.containsAll(areas)){
                areaRangeCopy.removeAll(areas);
                code.append(city.getCode()+ ",");
            }
        }

        System.out.println("结束");






    }

}
