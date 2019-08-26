package com.tzj.collect.api.iot.localmap;

import java.util.List;
import java.util.Map;
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

    public List<Map<String, Object>> nameList;//[{name:'怡宝'， quantity: 5},{name:'农妇三拳'， quantity: 10}]

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

    public List<Map<String, Object>> getNameList() {
        return nameList;
    }

    public void setNameList(List<Map<String, Object>> nameList) {
        this.nameList = nameList;
    }
}
