package com.tzj.collect.core.service;

import com.alipay.api.response.*;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Order;
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
     * 根据订单号查询是否有成功支付的订单 payment
     * @param orderNo
     * @return
     */
    Payment selectPayByOrderSn(String orderNo);

    String genalPay(Payment payment, Order order);

    String genalPayXcx(Payment payment,Order order) ;

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
    AlipayFundTransUniTransferResponse receivingMoneyTransfer(String aliUserId, String price, String outBizNo);
    /**
     * 交易关闭
     * @param outTradeNo
     * @return
     */
     AlipayTradeCloseResponse paymentCloseByTradeNo(String outTradeNo);
     
    /**
     * iot预付订单接口
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/9 0009
     * @Param: 
     * @return: 
     */
    String iotPay(String hardwareCode, String orderNo, String price, String outTradeNo);

     /**
      * iot定金回退接口
      * @author: sgmark@aliyun.com
      * @Date: 2019/11/15 0015
      * @Param: 
      * @return: 
      */
     AlipayFundTransUniTransferResponse iotTransfer(String aliUserId, String price, String outBizNo);

    Payment selectPayOneMinByOrderSn(String orderNo);
    void transferDemo(Payment payment);
}
