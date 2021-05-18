package com.tzj.collect.core.param.app;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPayParam {


    private int orderId;


    private BigDecimal price;

    private String voucherId;//优惠券Id

    private String outTradeNo;

    private BigDecimal totalFee;

    private String orderCode;

    private String openId;

    private String payBody;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
