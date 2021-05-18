package com.tzj.collect.core.service;

import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Payment;

public interface DailyPaymentService extends IService<Payment> {
    /** 答答答每日红包转账
     * @author sgmark@aliyun.com
     * @date 2019/8/15 0015
     * @param
     * @return
     */
    AlipayFundTransUniTransferResponse dailyDaTransfer(String aliUserId, String price, String outBizNo);
    //查询转账信息
    @DS("slave")
    AlipayFundTransOrderQueryResponse getTransfer(String orderSn);
    //增加积分
    void updateMemberPoint(String aliUserId, String OrderNo, double amount,String descrb);
}
