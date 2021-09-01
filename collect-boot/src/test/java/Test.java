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
import com.tzj.collect.Application;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.core.param.mysl.MyslBean;
import com.tzj.collect.core.param.mysl.MyslItemBean;
import com.tzj.collect.core.service.AnsycMyslService;
import com.tzj.collect.core.service.IotOrderDetailService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.core.service.PaymentService;
import com.tzj.collect.entity.Order;
import com.tzj.module.easyopen.exception.ApiException;
import lombok.SneakyThrows;
import org.aspectj.weaver.ast.Or;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    @Resource(name="certAlipayClient")
    AlipayClient certAlipayClient;
    @Autowired
    OrderService orderService;
    @Autowired
    AnsycMyslService ansycMyslService;
    @Autowired
    IotOrderDetailService iotOrderDetailService;

    @SneakyThrows
    @org.junit.Test
    public  void creatIotOrderByMqtt() {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        String sn ="test12345678901";
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
     * @return
     * @throws AlipayApiException
     */
    @org.junit.Test
    public void aliPayTransfer() throws AlipayApiException {
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo("trans_tzj_20210824002");
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        Participant payeeInfo =new Participant();
        payeeInfo.setIdentity("2088012579530754");
        payeeInfo.setIdentityType("ALIPAY_USER_ID");
        model.setTransAmount("15.00");
        model.setOrderTitle("垃圾分类回收(收呗)货款");
        model.setRemark("垃圾分类回收(收呗)货款");
        model.setPayeeInfo(payeeInfo);
        model.setBizScene("DIRECT_TRANSFER");
        request.setBizModel(model);
        System.out.println("转账结果："+JSONObject.toJSON(certAlipayClient.certificateExecute(request)).toString());

    }

    @SneakyThrows
    @org.junit.Test
    public  void transferQuery() {
        AlipayFundTransOrderQueryResponse response=paymentService.getTransfer("test1234567890");
        System.out.println(JSONObject.toJSON(response).toString());
    }



    @SneakyThrows
    @org.junit.Test
    public  void mysl() {
        List<Order> orders = orderService.getOrders();
        System.out.println("共获取到订单数量："+orders.size());

        orders.forEach(order -> {
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
            AlipayEcoActivityRecycleSendRequest request = new AlipayEcoActivityRecycleSendRequest();
            request.setBizContent(order.getMyslParam());
            AlipayEcoActivityRecycleSendResponse antMerchantExpandTradeorderSyncResponse = null;
            try {
                System.out.println("开始发放蚂蚁森林能量:"+ JSONObject.toJSON(request));
                antMerchantExpandTradeorderSyncResponse = alipayClient.execute(request);
                if (antMerchantExpandTradeorderSyncResponse.isSuccess()) {
                    order.setMyslOrderId(antMerchantExpandTradeorderSyncResponse.getFullEnergy()+"");
                    order.setMyslParam(JSONObject.toJSON(antMerchantExpandTradeorderSyncResponse.getParams()).toString());
                    orderService.updateById(order);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });

    }
}
