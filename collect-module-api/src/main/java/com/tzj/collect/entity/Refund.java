package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName(value = "wx_refund")
public class Refund extends DataEntity<Long> implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 外部流水号
     */
    @TableField(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 退款流水号
     */
    @TableField(value = "out_refund_no")
    private String outRefundNo;

    /**
     * 退款金额
     */
    @TableField(value = "refund_fee")
    private BigDecimal refundFee;

    /**
     * 订单总金额
     */
    @TableField(value = "total_fee")
    private BigDecimal totalFee;

    /**
     * 请求参数
     */
    @TableField(value = "request_params")
    private String requestParams;

    /**
     * 响应参数
     */
    @TableField(value = "response_params")
    private String responseParams;

    /**
     * 状态
     */
    @TableField(value = "status_")
    private String status;





    private static final long serialVersionUID = 1L;
}