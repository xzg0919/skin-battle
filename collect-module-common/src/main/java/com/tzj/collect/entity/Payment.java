package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付宝单
 */
@TableName("sb_payment")
@Data
public class Payment extends DataEntity<Long>{

    public final static  int STATUS_UNPAY=0;    //未支付
    public final static  int STATUS_PAYED=1;    //已支付
    public final static  int STATUS_TRANSFER=2; //已转账


    private Long id;

    private String aliUserId;
    private String tradeNo;
    private String notifyUrl;
    private String notifyTime;
    private BigDecimal price;
    private String buyerId;
    private String buyerLogonId;
    private String sellerId;

    private Long recyclersId;
    private String orderSn;
    //是否转账成功
    private String isSuccess;
    @TableField(value = "status_")
    private int status=STATUS_UNPAY;

    private PayType payType;//交易类型默认0:收呗订单; 1: 每日答答答红包转账

    @TableField(exist = false)
    private String totalAmount;
    @TableField(exist = false)
    private VoucherMember voucherMember;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAliUserId() {
        return aliUserId;
    }

    public void setAliUserId(String aliUserId) {
        this.aliUserId = aliUserId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Long getRecyclersId() {
        return recyclersId;
    }

    public void setRecyclersId(Long recyclersId) {
        this.recyclersId = recyclersId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public enum PayType implements IEnum {
        COLLECT(0), // 收呗订单
        RED_BAG(1), // 答答答红包转账
        CASH_BAG(2);//马上回收现金红包
        private int value;

        PayType(final int value) {
            this.value = value;
        }

        public Serializable getValue() {
            return this.value;
        }
    }
}
