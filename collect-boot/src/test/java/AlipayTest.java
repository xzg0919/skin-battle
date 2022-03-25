import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundCouponOrderAgreementPayModel;
import com.alipay.api.domain.AlipayFundCouponOrderDisburseModel;
import com.alipay.api.request.AlipayCommerceIndustryOrderSyncRequest;
import com.alipay.api.request.AlipayCommerceIndustryServiceSubmitRequest;
import com.alipay.api.request.AlipayFundCouponOrderAgreementPayRequest;
import com.alipay.api.request.AlipayFundCouponOrderDisburseRequest;
import com.alipay.api.response.AlipayCommerceIndustryOrderSyncResponse;
import com.alipay.api.response.AlipayCommerceIndustryServiceSubmitResponse;
import com.alipay.api.response.AlipayFundCouponOrderAgreementPayResponse;
import com.alipay.api.response.AlipayFundCouponOrderDisburseResponse;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.module.common.utils.DateUtils;
import lombok.Data;
import sun.nio.cs.ext.GBK;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class AlipayTest {


    /**
     * 低价值可回收物
     */
    public static final String LOW_VALUE_RECYCLE = "2022032221000149128445";
    /**
     * 家电回收
     */
    public static final String HOUSEHOLD_ELECTRICAL_APPLIANCES_RECYCLE = "2022032421000650389087";
    /**
     * 大件
     */
    public static final String BIG_GARBAGE_CHARGED_RECYCLE = "2022032221000749131061";

    /**
     * 衣物回收
     */
    public static final String CLOTHES_RECYCLE = "2022032221000249128451";

    public static void main(String[] args) throws AlipayApiException {
        AlipayTest alipayTest=new AlipayTest();
        alipayTest.test1();
    }


    public void test2() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient
                ("https://openapi.alipay.com/gateway.do",
                        AlipayConst.XappId, AlipayConst.private_key, "json",
                        "GBK", AlipayConst.ali_public_key, "RSA2");
        AlipayCommerceIndustryServiceSubmitRequest request = new AlipayCommerceIndustryServiceSubmitRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("service_type", "DOOR_RECYCLING");
        bizContent.put("service_name", "家电回收非公益回收订单回流");
        bizContent.put("service_description", "家电回收非公益回收订单回流");
        bizContent.put("service_action", "SERVICE_UPDATE");
        bizContent.put("service_url", "alipays://platformapi/startapp?appId=2018060660292753&page=pages/view/item-type/item-type%3Fid%3D22%26type%3Dappliance%26ss%3Dss");
        JSONObject industryInfo = new JSONObject();
        JSONObject platformInfo = new JSONObject();
        platformInfo.put("platform_name", "易代扔");
        platformInfo.put("platform_telephone", "400-686-1575");
        JSONObject serviceInfo = new JSONObject();
        serviceInfo.put("service_type", "HOUSEHOLD_ELECTRICAL_APPLIANCES_RECYCLE");
        serviceInfo.put("service_city", "610100,110100,310100,340600,340500,340200,320400,320100,320500,320200,321100,330700,370200,330600,130300,210800,371000,440700,440900,460400,510400,371100,442000,210200,210100,430200,440500,370600,440300,441900,440400");
        industryInfo.put("platform_info", platformInfo);
        industryInfo.put("service_info", serviceInfo);
        bizContent.put("industry_info", industryInfo);
        request.setBizContent(bizContent.toString());
        System.out.println(JSONObject.toJSONString(bizContent));
        AlipayCommerceIndustryServiceSubmitResponse execute = alipayClient.execute(request);
        System.out.println(JSONObject.toJSONString(execute));
    }

    public void test1() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient
                ("https://openapi.alipay.com/gateway.do",
                        AlipayConst.XappId, AlipayConst.private_key, "json",
                        "GBK", AlipayConst.ali_public_key, "RSA2");
        AlipayCommerceIndustryOrderSyncRequest request = new AlipayCommerceIndustryOrderSyncRequest();
        JSONObject bizContent = new JSONObject();
        //商户订单号
        bizContent.put("merchant_order_no", "202202240001");
        //服务类型
        /**
         * CLOTHES_RECYCLING 衣物回收
         * BOOK_RECYCLING 书籍回收
         * DIGITAL_RECYCLING 数码商品回收
         * HOUSEHOLD_APPLIANCES_RECYCLING 家电回收
         */
        bizContent.put("service_type", "HOUSEHOLD_APPLIANCES_RECYCLING");
        bizContent.put("buyer_id", "2088212017275410");
        //服务标识
        bizContent.put("service_code", HOUSEHOLD_ELECTRICAL_APPLIANCES_RECYCLE);
        bizContent.put("order_source", "ALIPAY_APPLETS");
        bizContent.put("status", "CREATE");
        bizContent.put("order_create_time", DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        bizContent.put("order_modify_time", DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        bizContent.put("order_detail_url", "alipays://platformapi/startapp?appId=2018060660292753&page=pages/view/orderDetails/appliances/appliances%3Fid%3D10086");
        bizContent.put("order_amount", "1");
        bizContent.put("payment_amount","1");
        JSONObject industryInfo = new JSONObject();
        JSONObject serviceProductInfo = new JSONObject();
        serviceProductInfo.put("goods_name", "冰箱");
        serviceProductInfo.put("goods_desc", "冰箱");
        serviceProductInfo.put("quantity", "1");
        industryInfo.put("service_product_info", serviceProductInfo);
        JSONObject servicePoviderInfo = new JSONObject();
        servicePoviderInfo.put("platform_name", "易代扔");
        servicePoviderInfo.put("platform_phone", "400-686-1575");
        industryInfo.put("service_provider_info", servicePoviderInfo);
        JSONObject servicePerformanceInfo = new JSONObject();
        JSONObject appointmentTime = new JSONObject();
        appointmentTime.put("start_time", "2022-03-25 11:00:00");
        appointmentTime.put("end_time", "2022-03-25 18:00:00");
        servicePerformanceInfo.put("appointment_time", appointmentTime);
        industryInfo.put("service_performance_info", servicePerformanceInfo);
        bizContent.put("industry_info", industryInfo);
        request.setBizContent(bizContent.toJSONString());
        System.out.println(request.getBizContent());
        AlipayCommerceIndustryOrderSyncResponse response = alipayClient.execute(request,"composeBbe29a47edda04836a0f17bb67d803C41");
        System.out.println(JSONObject.toJSONString(response));
    }

    @Data
    class OrderSyncBizContent{

        String merchant_order_no;
        String service_type =HOUSEHOLD_ELECTRICAL_APPLIANCES_RECYCLE;
        String buyer_id;
        String service_code ;
        String order_source ="ALIPAY_APPLETS";
    }



}
