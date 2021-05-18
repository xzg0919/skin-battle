package com.tzj.collect.core.service;

import com.tzj.collect.entity.WxTransfer;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020-03-18 17:43
 * @Description:
 */
public interface WXPayService {
    Object prePay(String openId, String outTradeNo, String from, BigDecimal totalFee, String payBody, String tradeType);

    boolean queryOrder(String transactionId, String outTradeNo);

    void transfer(String partnerTardeNo, String openId, BigDecimal amount, String desc, String from);

    void transferRetry(WxTransfer  jlTransfer);

    Object transferQuery(String partnerTradeNo,String from);

    boolean refund(String outTradeNo,BigDecimal totalFee,BigDecimal refundFee);

    void orderComplete(String orderCode, String totalFee, MqttClient mqttClient);
}