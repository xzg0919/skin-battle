package com.tzj.collect.core.param.app;

import com.tzj.module.easyopen.doc.annotation.ApiDocField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPayParam {

    @ApiDocField(description = "收呗订单ID")
    private int orderId;

    @ApiDocField(description = "支付金额")
    private BigDecimal price;

    private String voucherId;//优惠券Id

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
