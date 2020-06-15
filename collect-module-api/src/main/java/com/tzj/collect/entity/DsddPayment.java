package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 定时订单支付
 */
@TableName("dsdd_payment")
@Data
public class DsddPayment extends DataEntity<Long> {



      Long id;

    /**
     * 支付通知时间
     */
    Date notifyTime;
    /**
     * 支付金额
     */
    BigDecimal price;

    /**
     * 支付流水号
     */
    String outTradeNo;

    /**
     * 回收人员id
     */
    Long recyclersId;







    /**
     * 支付宝交易流水号
     */
    String tradeNo;




}
