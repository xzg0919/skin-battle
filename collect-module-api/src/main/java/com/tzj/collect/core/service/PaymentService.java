package com.tzj.collect.core.service;

import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Payment;

public interface PaymentService extends IService<Payment> {

    /**
     * 根据订单号查询 payment
     * @param orderNo
     * @return
     */
    Payment selectByOrderSn(String orderNo);
    /**
     * 根据外部订单号查询 payment
     * @param outTradeNo
     * @return
     */
    Payment selectByOutTradeNo(String outTradeNo);
    /**
     * 根据订单号查询是否又成功支付的订单 payment
     * @param orderNo
     * @return
     */
    Payment selectPayByOrderSn(String orderNo);

    String genalPay(Payment payment);

    String genalPayXcx(Payment payment) ;

    /**
     * 转账
     * @param payment
     */
    void transfer(Payment payment);
    /**
     * 根据支付宝交易号查询该交易的详细信息
     * @param tradeNo
     */
    AlipayTradeQueryResponse getAliPayment(String tradeNo);
    /**
     * 查询转账信息
     * @param paymentId
     */
    AlipayFundTransOrderQueryResponse getTransfer(String paymentId);

    /** 答答答每日红包转账
      * @author sgmark@aliyun.com
      * @date 2019/8/15 0015
      * @param
      * @return
      */
    AlipayFundTransToaccountTransferResponse receivingMoneyTransfer(String aliUserId, String price, String outBizNo);
    /**
     * 交易关闭
     * @param outTradeNo
     * @return
     */
     AlipayTradeCloseResponse paymentCloseByTradeNo(String outTradeNo);
}
