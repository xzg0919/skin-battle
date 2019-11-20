package com.tzj.collect.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.alipay.api.response.AlipayOpenAppMiniTemplatemessageSendResponse;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.core.service.DailySendMessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 答答答memberService
 *
 * @author sgmark
 * @create 2019-08-29 9:35
 *
 */
@Service
@Transactional(readOnly = true)
public class DailySendMessageServiceImpl implements DailySendMessageService {

    public static void pushMessage() {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayOpenAppMiniTemplatemessageSendRequest request = new AlipayOpenAppMiniTemplatemessageSendRequest();
        AlipayOpenAppMiniTemplatemessageSendModel model = new AlipayOpenAppMiniTemplatemessageSendModel();
        model.setToUserId("2088212384105273");
        model.setFormId("MjA4ODIxMjM4NDEwNTI3M18xNTY3NzQwMTQ5MzI5XzAzMw==");
        model.setUserTemplateId("YjA0MjI0MmEwMjYxYmE0ZGZjMWU4M2RhMWZjNDg2MjE=");
        model.setPage("pages/view/index/index");
        model.setData("{\"keyword1\" :{\"value\":\"" + "每日答答答" + "\"},\"keyword2\" :{\"value\":\"" + "答题赢豪礼，更有现金红包等你来拿" + "\"},\"keyword3\" :{\"value\":\"" + "支付宝" + "\"}}");
        System.out.println(JSON.toJSONString(model));
        request.setBizModel(model);
        AlipayOpenAppMiniTemplatemessageSendResponse response = null;
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

    public static void main(String[] args) {
        pushMessage();
    }

    @Override
    public void sendMsgToAllMember(String aliUserId, String formId) {
        if (StringUtils.isEmpty(aliUserId) || StringUtils.isEmpty(formId)) {
            return;
        }
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayOpenAppMiniTemplatemessageSendRequest request = new AlipayOpenAppMiniTemplatemessageSendRequest();
        AlipayOpenAppMiniTemplatemessageSendModel model = new AlipayOpenAppMiniTemplatemessageSendModel();
        model.setToUserId(aliUserId);
        model.setFormId(formId);
        model.setUserTemplateId("YjA0MjI0MmEwMjYxYmE0ZGZjMWU4M2RhMWZjNDg2MjE=");
        model.setPage("pages/view/index/index");
        model.setData("{\"keyword1\" :{\"value\":\"每日答答答\"},\"keyword2\" :{\"value\":\"答题赢豪礼，更有现金红包等你来拿\"},\"keyword3\" :{\"value\":\"支付宝\"}}");
        System.out.println(JSON.toJSONString(model));
        request.setBizModel(model);
        AlipayOpenAppMiniTemplatemessageSendResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            System.out.println(response.getSubMsg());
        }
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}
