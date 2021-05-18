package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.entity.WxPayment;

public interface WxPaymentService extends IService<WxPayment>{


    WxPayment findByOutTradeNo(String outTradeNo);

    /**
     * 转账
     * @param payment
     */
    void transfer(WxPayment payment);
}
