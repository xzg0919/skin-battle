package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.internal.util.codec.Base64;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.alipay.api.response.AlipayOpenAppMiniTemplatemessageSendResponse;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.AsyncService;
import com.tzj.collect.common.notify.DingTalkNotify;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author 胡方明（12795880@qq.com）
 *
 * 异步通知
 *
 *
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async
    public void notifyDingDingOrderCreate(OrderBean orderBean) {
        StringBuffer message = new StringBuffer();
        message.append("收呗有新订单啦！\r\n");
        message.append("订单号：").append(orderBean.getOrderNo()).append("\r\n");
        message.append("企业：").append(orderBean.getCompanyName());
        ArrayList<String> atMobiles = new ArrayList<>();
        atMobiles.add("15996602085");
        atMobiles.add("13501675585");
        atMobiles.add("15901929893");
        atMobiles.add("17702196566");
        atMobiles.add("17717939653");
        atMobiles.add("13301857305");
        atMobiles.add("13855829796");
        atMobiles.add("15121063188");
        atMobiles.add("13262790702");
        String ddUrl = this.getDingDingUrl(orderBean.getDingDingUrl(), orderBean.getDingDingSing());
        DingTalkNotify.sendTextMessageWithAt(message.toString(), atMobiles, ddUrl);
    }

    @Override
    @Async
    public void notifyDingDingPaymentError(String orderNo, String reason, String dingDingUrl, String dingDingSing, String receiveTel) {
        StringBuffer message = new StringBuffer();
        message.append("转账有异常了！\r\n");
        message.append("订单号：").append(orderNo).append("\r\n");
        message.append("内容：").append(reason);
        ArrayList<String> atMobiles = new ArrayList<>();
        atMobiles.add(receiveTel);
        String ddUrl = this.getDingDingUrl(dingDingUrl, dingDingSing);
        DingTalkNotify.sendTextMessageWithAt(message.toString(), atMobiles, ddUrl);
    }

    public String getDingDingUrl(String ddUrl, String ddSing) {
        if (StringUtils.isBlank(ddSing)) {
            return ddUrl;
        }
        try {
            String secret = ddSing;
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sing = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            ddUrl = ddUrl + "&timestamp=" + timestamp + "&sign=" + sing;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ddUrl;
    }

    @Async
    @Override
    public void notifyDingDingOrderCreate(String message, boolean atAll, String dingDingUrl) {
        DingTalkNotify.sendTextMessageWithAtAndAtAll(message, null, atAll, dingDingUrl);
    }

    /**
     *
     * <p>
     * Created on 2017年8月14日</p>
     * <p>
     * Description:[发送接单短信]</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     * @param recyclerName : 回收人员名称
     * @param phone : 回收人员手机号
     * @param companyName : 企业名称
     */
    @Override
    @Async
    public void sendOrder(final String freeSignName, final String moblie, final String temlateCode,
            final String recyclerName, final String phone, final String companyName) {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"name\":\"" + recyclerName + "\",\"phone\":\"" + phone + "\",\"company\":\"" + companyName + "\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * <p>
     * Created on 2017年8月14日</p>
     * <p>
     * Description:[发送发货短信]</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     */
    @Override
    @Async
    public void sendOrderProduct(final String freeSignName, final String moblie, final String temlateCode,
            final String goodsName, final String orderCompany, final String orderNum) {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"goods\":\"" + goodsName + "\",\"order_company\":\"" + orderCompany + "\",\"order_num\":\"" + orderNum + "\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * <p>
     * Created on 2017年8月14日</p>
     * <p>
     * Description:[发送发货短信]</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     */
    @Override
    @Async
    public void sendEnterprise(final String freeSignName, final String moblie, final String temlateCode,
            final String code, final String productName) {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"code\":\"" + code + "\",\"productName\":\"" + productName + "\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendOpenAppMini(String aliUserId, String formId, String templateId, String page, String value1, String value2, String value3) {
        if (StringUtils.isNotBlank(formId)){
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
            AlipayOpenAppMiniTemplatemessageSendRequest request = new AlipayOpenAppMiniTemplatemessageSendRequest();
            AlipayOpenAppMiniTemplatemessageSendModel model = new AlipayOpenAppMiniTemplatemessageSendModel();
            model.setToUserId(aliUserId);
            model.setFormId(formId);
            model.setUserTemplateId(templateId);
            model.setPage(page);
            model.setData("{\"keyword1\" :{\"value\":\"" + value1 + "\"},\"keyword2\" :{\"value\":\"" + value2 + "\"},\"keyword3\" :{\"value\":\"" + value3 + "\"}}");
            System.out.println(JSON.toJSONString(model));
            request.setBizModel(model);
            AlipayOpenAppMiniTemplatemessageSendResponse response = null;
            try {
                response = alipayClient.execute(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
            System.out.println(response.getBody());
        }
    }

}
