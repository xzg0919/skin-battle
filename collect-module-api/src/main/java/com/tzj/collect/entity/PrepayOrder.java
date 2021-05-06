package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName(value = "wx_prepay_order")
public class PrepayOrder extends DataEntity<Long> implements Serializable {
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
     * open_id
     */
    @TableField(value = "open_id")
    private String openId;

    /**
     * 来源
     */
    @TableField(value = "from_")
    private String from;

    /**
     * 状态
     */
    @TableField(value = "status_")
    private String status;

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
     * 交易金额（分）
     */
    @TableField(value = "totalFee")
    private BigDecimal totalfee;

    /**
     * 订单号
     */
    private String orderCode;




    private static final long serialVersionUID = 1L;


    /**
     *  生成预付订单失败
     */
    public static final String INIT_FAIL="INIT_FAIL";

    /**
     * 生成预付订单成功
     */
    public static final String INIT_SUCCESS="INIT_SUCCESS";


    /**
     * 支付类型
     */
    public static  final String COLLECT="COLLECT";
    public static  final String MALL="MALL";


    private String tradeType;
}