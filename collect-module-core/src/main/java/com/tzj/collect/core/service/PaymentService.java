package com.tzj.collect.core.service;

import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
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
     * @param tradeNo
     */
    AlipayFundTransOrderQueryResponse getTransfer(String paymentId);
}
