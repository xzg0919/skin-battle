package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020-03-19 10:30
 * @Description:
 */
@Data
@TableName(value = "wx_payment")
public class WxPayment extends DataEntity<Long> {

    private Long id;
    private String openId;
    private String orderNo;
    private BigDecimal totalFee;
    private String payStatus;
    private String outTradeNo;
    private String transactionId;
    @TableField(exist = false)
    private String totalAmount;
    @TableField(exist = false)
    private VoucherMember voucherMember;
    @TableField(exist = false)
    private BigDecimal transferPrice;
    @TableField(exist = false)
    private BigDecimal discountPrice;


    public static final String SUCCESS="SUCCESS";
    public static final String CANCEL="CANCEL";


}
