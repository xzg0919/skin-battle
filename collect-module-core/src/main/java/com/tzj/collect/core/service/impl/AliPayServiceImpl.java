package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.mq.RocketMqConst;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Member;
import com.tzj.collect.common.notify.DingTalkNotify;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliPayServiceImpl implements AliPayService {

    /**
     * 根据用户授权的具体authCode查询是用户的userid和token
     *
     * @author 王灿
     * @return
     */
    @Override
    public AlipaySystemOauthTokenResponse selectUserToken(String userCode, String appId) {
        System.out.println("-------hua用户信息接口 userCode是：" + userCode);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, appId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(userCode);
        request.setGrantType("authorization_code");
        AlipaySystemOauthTokenResponse response = null;
        try {
            response = alipayClient.execute(request);
            //用户的授权的token
            System.out.println(response.getAccessToken());
            //用户的唯一userId
            System.out.println(response.getUserId());
        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用用户查询token接口成功");
        } else {
            System.out.println("调用用户查询token接口失败");
        }
        return response;
    }

    @Override
    public AlipaySystemOauthTokenResponse flcxToken(String userCode, String appId) {
        System.out.println("-------hua用户信息接口 userCode是：" + userCode);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, appId, AlipayConst.flcx_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.flcx_ali_public_key, AlipayConst.sign_type);
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(userCode);
        request.setGrantType("authorization_code");
        AlipaySystemOauthTokenResponse response = null;
        try {
            response = alipayClient.execute(request);
            //用户的授权的token
            System.out.println(response.getAccessToken());
            //用户的唯一userId
            System.out.println(response.getUserId());
        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用用户查询token接口成功");
        } else {
            System.out.println("调用用户查询token接口失败");
        }
        return response;
    }

    /**
     * 调用接口查询用户的详细信息
     *
     * @author 王灿
     * @return
     */
    @Override
    public AlipayUserInfoShareResponse selectUser(String userToken, String appId) {
        System.out.println("-------进入了查询用户信息接口 token是：" + userToken);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, appId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayUserInfoShareRequest userinfoRequest = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse response = null;
        try {
            response = alipayClient.execute(userinfoRequest, userToken);
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用查询用户信息的接口成功");
        } else {
            System.out.println("调用查询用户信息的接口失败");

        }
        return response;
    }

    /**
     *
     * <p>
     * Discription:[发放会员卡]</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return Map<String,Object>
     */
    @Override
    public Map<String, Object> send(String accessToken, String userId,
            String cardNo, String point, String templateId, String balance, String vip, String appId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (null == point || "".equals(point) || point.contains("-")) {
            point = "0";
        }
        if (StringUtils.isBlank(balance)) {
            balance = "0";
        }
        String usefullDate[] = getUsefullDate(null);
        String bizContent = "{";
        bizContent = bizContent
                + "\"out_serial_no\":\""
                + UUID.randomUUID().toString().replaceAll("-", "")
                + "\",\"card_template_id\":\""
                + templateId
                + "\",\"card_user_info\":{"
                + "\"user_uni_id\":\""
                + userId
                + "\",\"user_uni_id_type\":\"UID\"},"
                + "\"card_ext_info\":{"
                + " \"external_card_no\":\""
                + cardNo
                + "\","
                + "\"open_date\":\""
                + usefullDate[0]
                + "\","
                + "\"valid_date\":\""
                + usefullDate[1]
                + "\",";
        if (!StringUtils.isBlank(vip)) {
            bizContent = bizContent
                    + "\"level\":\""
                    + vip
                    + "\",";
        }
        bizContent = bizContent
                + "\"point\":\""
                + point
                + "\","
                + "\"balance\":\""
                + balance
                + "\""
                + "}}";
        try {
            AlipayMarketingCardOpenRequest request = new AlipayMarketingCardOpenRequest();
            request.setBizContent(bizContent);
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, appId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
            AlipayMarketingCardOpenResponse response = alipayClient.execute(request, accessToken);
            if (null != response) {
                if (response.isSuccess()) {
                    returnMap.put("bizCardNo", response.getCardInfo().getBizCardNo());
                    returnMap.put("openDate", response.getCardInfo().getOpenDate());
                } else {
                    System.out.println("发放会员卡失败-------------------");
                    returnMap.put("msg", response.getSubMsg());
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    //删除会员卡
    @Override
    public void deleteCard(Member member) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", member.getAppId(), AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayMarketingCardDeleteRequest request = new AlipayMarketingCardDeleteRequest();
        AlipayMarketingCardDeleteModel model = new AlipayMarketingCardDeleteModel();
        model.setOutSerialNo(UUID.randomUUID().toString());
        model.setTargetCardNo(member.getAliCardNo());
        model.setTargetCardNoType("BIZ_CARD");
        model.setReasonCode("USER_UNBUND");
        request.setBizModel(model);
        AlipayMarketingCardDeleteResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * <p>
     * Discription:[会员卡有效期，十年]</p>
     *
     * @author:[王灿][yanghuan1937@aliyun.com]
     * @udate:[日期YYYY-MM-DD] [更改人姓名]
     * @return String []
     */
    private String[] getUsefullDate(Date startDate) {
        Calendar now = Calendar.getInstance();
        if (null != startDate) {
            now.setTime(startDate);
        }
        String usefullDate[] = new String[2];
        usefullDate[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) + 10);
        usefullDate[1] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
        return usefullDate;
    }

    private static String[] getUsefullDates(Date startDate) {
        Calendar now = Calendar.getInstance();
        if (null != startDate) {
            now.setTime(startDate);
        }
        String usefullDate[] = new String[2];
        usefullDate[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) + 10);
        usefullDate[1] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
        return usefullDate;
    }

//    public static void main(String[] args) throws Exception{
//        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, "2018060660292753", AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
//        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
//        request.setCode("c5de4d7a5b4642129b326e0aab49OX50");
//        request.setGrantType("authorization_code");
//        AlipaySystemOauthTokenResponse response=null;
//        try {
//            response = alipayClient.execute(request);
//            //用户的授权的token
//            System.out.println(response.getAccessToken());
//            //用户的唯一userId
//            System.out.println(response.getUserId());
//        } catch (AlipayApiException e) {
//            //处理异常
//            e.printStackTrace();
//        }
//        if(response.isSuccess()){
//            System.out.println("调用用户查询token接口成功");
//        } else {
//            System.out.println("调用用户查询token接口失败");
//        }
//    }
    /**
     * <p>
     * Discription:[更改会员积分]</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public String updatePoint(String targetCardNo, Date openDate, String point, String vip, String appId) {
        String bizContent = "{";
        String usefullDate[] = getUsefullDate(openDate);
        if (null != point && point.contains("-")) {
            point = "0";
        }
        bizContent = bizContent
                + "\"target_card_no\":\""
                + targetCardNo
                + "\","
                + "\"target_card_no_type\":\"BIZ_CARD\","
                + "\"occur_time\":\""
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                + "\","
                + "\"card_info\":{"
                + "\"open_date\":\""
                + usefullDate[0]
                + "\","
                + "\"valid_date\":\""
                + usefullDate[1]
                + "\",";
        if (!StringUtils.isBlank(vip)) {
            bizContent = bizContent
                    + "\"level\":\""
                    + vip
                    + "\",";
        }
        bizContent = bizContent
                + "\"point\":\""
                + point
                + "\","
                + "\"balance\":\"0\""
                + "},"
                + "\"ext_info\":\"\\\"\\\"\""
                + "}";
        try {
            AlipayMarketingCardUpdateRequest request = new AlipayMarketingCardUpdateRequest();
            request.setBizContent(bizContent);
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, appId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
            AlipayMarketingCardUpdateResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                System.out.println("更新会员卡积分失败");
                DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), "更新会员卡积分失败",
                        RocketMqConst.DINGDING_ERROR, response.getBody());
            } else {
                System.out.println("更新会员卡积分成功");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return vip;
    }

    /**
     * <p>
     * 芝麻认证初始化接口</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public ZhimaCustomerCertificationInitializeResponse initialize(String certName, String certNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.TZJ_appId, AlipayConst.TZJ_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.TZJ_ali_public_key, AlipayConst.sign_type);
        ZhimaCustomerCertificationInitializeRequest request = new ZhimaCustomerCertificationInitializeRequest();
        ZhimaCustomerCertificationInitializeModel model = new ZhimaCustomerCertificationInitializeModel();
        model.setTransactionId(System.currentTimeMillis() + "" + (new Random().nextInt(999999) + 100000));
        model.setProductCode("w1010100000000002978");
        model.setBizCode("FACE");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("identity_type", "CERT_INFO");
        map.put("cert_type", "IDENTITY_CARD");
        map.put("cert_name", certName);
        map.put("cert_no", certNo);
        model.setIdentityParam(JSON.toJSONString(map));
        model.setExtBizParam(null);
        request.setBizModel(model);

        ZhimaCustomerCertificationInitializeResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (Exception e) {
        }
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用芝麻认证初始化接口成功");
        } else {
            System.out.println("调用芝麻认证初始化接口失败");
        }
        return response;
    }

    /**
     * <p>
     * 芝麻认证初返回URL</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public ZhimaCustomerCertificationCertifyResponse getInitializeUrl(String bizNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.TZJ_appId, AlipayConst.TZJ_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.TZJ_ali_public_key, AlipayConst.sign_type);
        ZhimaCustomerCertificationCertifyRequest request1 = new ZhimaCustomerCertificationCertifyRequest();
        ZhimaCustomerCertificationCertifyModel model1 = new ZhimaCustomerCertificationCertifyModel();
        model1.setBizNo(bizNo);
        request1.setBizModel(model1);
        request1.setReturnUrl("xl://goods:8888/goodsDetail?goodsId=10011002");
        // 设置业务参数,必须要biz_no
        //request1.setBizContent("{\"biz_no\":\"ZM201611103000000888800000733621\"}");
        // 设置回调地址,必填. 如果需要直接在支付宝APP里面打开回调地址使用alipay协议
        // alipay://www.taobao.com 或者 alipays://www.taobao.com,分别对应http和https请求
        //request1.setReturnUrl("alipays://www.taobao.com");

        // 这里一定要使用GET模式
        ZhimaCustomerCertificationCertifyResponse response1 = null;
        try {
            response1 = alipayClient.pageExecute(request1, "GET");

        } catch (Exception e) {

        }
        // 从body中获取URL
        String url = response1.getBody();
        System.out.println("generateCertifyUrl url:" + url);
        return response1;
    }

    /**
     * <p>
     * 芝麻认证初开始接口</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public ZhimaCustomerCertificationQueryResponse certify(String bizNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.TZJ_appId, AlipayConst.TZJ_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.TZJ_ali_public_key, AlipayConst.sign_type);
        ZhimaCustomerCertificationQueryRequest request = new ZhimaCustomerCertificationQueryRequest();
        ZhimaCustomerCertificationQueryModel model = new ZhimaCustomerCertificationQueryModel();
        model.setBizNo(bizNo);
        request.setBizModel(model);
        ZhimaCustomerCertificationQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用芝麻认证初开始接口成功");
        } else {
            System.out.println("调用芝麻认证初开始接口失败");
        }
        return response;
    }

    @Override
    public AlipayMarketingCardQueryResponse getPassIdUrl(String aliCardNo, String aliUserId) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayMarketingCardQueryRequest request = new AlipayMarketingCardQueryRequest();
        AlipayMarketingCardQueryModel model = new AlipayMarketingCardQueryModel();
        model.setTargetCardNoType("BIZ_CARD");
        model.setTargetCardNo(aliCardNo);
        CardUserInfo cardUserInfo = new CardUserInfo();
        cardUserInfo.setUserUniId(aliUserId);
        cardUserInfo.setUserUniIdType("UID");
        model.setCardUserInfo(cardUserInfo);
        request.setBizModel(model);
        AlipayMarketingCardQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用成功" + response.getBody());
        } else {
            System.out.println("调用失败");
        }
        return response;
    }

    /**
     * 人脸认证身份认证初始化服务（支付宝开放认证初始化服务）
     *
     * @author sgmark@aliyun.com
     * @date 2019/8/30 0030
     * @param
     * @return
     */
    @Override
    public AlipayUserCertifyOpenInitializeResponse initializeAlipayUser(String certName, String certNo) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayUserCertifyOpenInitializeRequest request = new AlipayUserCertifyOpenInitializeRequest();
        //构造身份信息json对象
        JSONObject identityObj = new JSONObject();
        //身份类型，必填，详细取值范围请参考接口文档说明
        identityObj.put("identity_type", "CERT_INFO");
        //证件类型，必填，详细取值范围请参考接口文档说明
        identityObj.put("cert_type", "IDENTITY_CARD");
        //真实姓名，必填
        identityObj.put("cert_name", certName);
        //证件号码，必填
        identityObj.put("cert_no", certNo);
        //构造商户配置json对象
        JSONObject merchantConfigObj = new JSONObject();
        // 设置回调地址,必填. 如果需要直接在支付宝APP里面打开回调地址使用alipay协议，参考下面的案例：appId用固定值 20000067，url替换为urlEncode后的业务回跳地址
        // alipays://platformapi/startapp?appId=20000067&url=https%3A%2F%2Fapp.cqkqinfo.com%2Fcertify%2FzmxyBackNew.do
        merchantConfigObj.put("return_url", "xl://goods:8888/goodsDetail?goodsId=10011002");

        //构造身份认证初始化服务业务参数数据
        JSONObject bizContentObj = new JSONObject();
        //商户请求的唯一标识，推荐为uuid，必填
        bizContentObj.put("outer_order_no", UUID.randomUUID());
        bizContentObj.put("biz_code", "FACE");
        bizContentObj.put("identity_param", identityObj);
        bizContentObj.put("merchant_config", merchantConfigObj);
        request.setBizContent(bizContentObj.toString());
        AlipayUserCertifyOpenInitializeResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        System.out.println(response.getCertifyId());
        return response;
    }

    /**
     * 身份认证开始认证服务接口
     *
     * @author sgmark@aliyun.com
     * @date 2019/8/30 0030
     * @param
     * @return
     */
    @Override
    public AlipayUserCertifyOpenCertifyResponse certifyAlipayUser(String certifyId) {
        //获取alipay client
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayUserCertifyOpenCertifyRequest request = new AlipayUserCertifyOpenCertifyRequest();

        //设置certifyId
        JSONObject bizContentObj = new JSONObject();
        bizContentObj.put("certify_id", certifyId);
        request.setBizContent(bizContentObj.toString());

        //生成请求链接，这里一定要使用GET模式
        AlipayUserCertifyOpenCertifyResponse response = null;
        try {
            response = alipayClient.pageExecute(request, "GET");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("开始认证服务调用成功");
            String certifyUrl = response.getBody();
            System.out.println(certifyUrl);
            //执行后续流程...
        } else {
            System.out.println("调用失败");
        }
        return response;
    }

    @Override
    public AlipayUserCertifyOpenQueryResponse certifyOpenQuery(String certifyId) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayUserCertifyOpenQueryRequest request = new AlipayUserCertifyOpenQueryRequest();
        request.setBizContent("{"
                + "\"certify_id\":\"" + certifyId + "\" }");
        AlipayUserCertifyOpenQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.iotAppId, AlipayConst.iot_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.iot_ali_public_key, AlipayConst.sign_type);
        ZolozAuthenticationCustomerSmilepayInitializeRequest request = new ZolozAuthenticationCustomerSmilepayInitializeRequest();
        request.setBizContent("{\"apdidToken\":\"Hk4Flm7iFcTVXAV3qg5GUsDUi-Ua4tSB9LYcxVxbYfErUqivbgEAAA==\"" +
                ",\"appName\":\"com.alipay.zoloz.smile\"" +
                ",\"appVersion\":\"3.10.0.354\"" +
                ",\"bioMetaInfo\":\"4.2.0:287358976,2\"" +
                ",\"deviceModel\":\"msm8953 for arm64\"" +
                ",\"deviceType\":\"android\"" +
                ",\"machineInfo\":{\"cameraDriveVer\":\"\",\"cameraModel\":\"AstraD2\",\"cameraName\":\"AstraD2\",\"cameraVer\":\"\",\"ext\":\"\",\"group\":\"\",\"machineCode\":\"Xd3k14OaCg4DAJ61ZbLEYeV3\",\"machineModel\":\"msm8953 for arm64\",\"machineVer\":\"7.1.2\"}" +
                ",\"merchantInfo\":{\"alipayStoreCode\":\"TEST\",\"appId\":\"2019110668924840\",\"areaCode\":\"TEST\",\"brandCode\":\"TEST\",\"deviceMac\":\"TEST\",\"deviceNum\":\"TEST_ZOLOZ_TEST\",\"geo\":\"0.000000,0.000000\",\"merchantId\":\"2088421446748170\",\"partnerId\":\"2088421446748170\",\"storeCode\":\"TEST\",\"wifiMac\":\"TEST\",\"wifiName\":\"TEST\"}" +
                ",\"osVersion\":\"7.1.2\"" +
                ",\"remoteLogID\":\"bb9bc74d22e2448498dd302f93ff735d1275978627\"" +
                ",\"zimVer\":\"1.0.0\"}");
        ZolozAuthenticationCustomerSmilepayInitializeResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
    /**
     * 刷脸支付初始化
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/26 0026
     * @Param: 
     * @return: 
     */
    @Override
    public ZolozAuthenticationCustomerSmilepayInitializeResponse smilePayInitialize(String apdidToken, String appName, String appVersion, String bioMetaInfo){
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.iotAppId, AlipayConst.iot_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.iot_ali_public_key, AlipayConst.sign_type);
        ZolozAuthenticationCustomerSmilepayInitializeRequest request = new ZolozAuthenticationCustomerSmilepayInitializeRequest();
        request.setBizContent("{\"apdidToken\":" + apdidToken +
                ",\"appName\":" + appName +
                ",\"appVersion\":" + appVersion +
                ",\"bioMetaInfo\":" + bioMetaInfo +
                ",\"deviceModel\":\"msm8953 for arm64\"" +
                ",\"deviceType\":\"android\"" +
                ",\"machineInfo\":{\"cameraDriveVer\":\"\",\"cameraModel\":\"AstraD2\",\"cameraName\":\"AstraD2\",\"cameraVer\":\"\",\"ext\":\"\",\"group\":\"\",\"machineCode\":\"Xd3k14OaCg4DAJ61ZbLEYeV3\",\"machineModel\":\"msm8953 for arm64\",\"machineVer\":\"7.1.2\"}" +
                ",\"merchantInfo\":{\"alipayStoreCode\":\"TEST\",\"appId\":\"2019110668924840\",\"areaCode\":\"TEST\",\"brandCode\":\"TEST\",\"deviceMac\":\"TEST\",\"deviceNum\":\"TEST_ZOLOZ_TEST\",\"geo\":\"0.000000,0.000000\",\"merchantId\":\"2088421446748170\",\"partnerId\":\"2088421446748170\",\"storeCode\":\"TEST\",\"wifiMac\":\"TEST\",\"wifiName\":\"TEST\"}" +
                ",\"osVersion\":\"7.1.2\",\"remoteLogID\":\"bb9bc74d22e2448498dd302f93ff735d1275978627\",\"zimVer\":\"1.0.0\"}");
        ZolozAuthenticationCustomerSmilepayInitializeResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response;
    }
    /**
     *人脸ftoken查询消费接口(获取uId)
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/26 0026
     * @Param: 
     * @return:
     */
    @Override
    public ZolozAuthenticationCustomerFtokenQueryResponse  customerFtokenQuery(String fToken){
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.iotAppId, AlipayConst.iot_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.iot_ali_public_key, AlipayConst.sign_type);
        ZolozAuthenticationCustomerFtokenQueryRequest request = new ZolozAuthenticationCustomerFtokenQueryRequest();
        request.setBizContent("{" +
                "\"ftoken\":" + fToken + "," +
                "\"biz_type\":\"1\"," +
                "\"ext_info\":{" +
                "\"query_type\":\"\"" +
                "    }" +
                "  }");
        ZolozAuthenticationCustomerFtokenQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response;
    }
}
