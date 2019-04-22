package com.tzj.collect.api.iot.localmap;

import java.util.concurrent.CountDownLatch;

/**
 * 线程
 *
 * @author sgmark
 * @create 2019-04-22 10:30
 **/
public class LatchMap implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public CountDownLatch latch;//线程同步

    public String orderId;//需要返回的订单编号


    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
