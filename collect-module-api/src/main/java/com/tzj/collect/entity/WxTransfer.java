package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020-03-20 14:48
 * @Description:
 */
@Data
@TableName("wx_transfer")
public class WxTransfer extends DataEntity<Long> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 转账商户流水号
     */
    String partnerTradeNo;

    String openId;

    BigDecimal amount;

    /**
     * 应用id
     */
    String mchAppid;

    /**
     * 商户id
     */
    String mchid;


    String resultCode;

    /**
     * 微信付款单号
     */
    String paymentNo;


    /**
     * 请求参数
     */
    String requestParams;

    /**
     * 返回参数
     */
    String responseParams;


    /**
     * 订单渠道
     */
    @TableField(value = "from_")
    String from ;


}
