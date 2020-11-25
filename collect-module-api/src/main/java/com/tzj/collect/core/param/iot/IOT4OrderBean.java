package com.tzj.collect.core.param.iot;

import lombok.Data;


/**
 * @Auther: xiangzhongguo
 * @Date: 2020/11/20 10:39
 * @Description:
 */
@Data
public class IOT4OrderBean {


    /**
     * 设备号
     */
    String deviceCode;


    /**
     * 订单号
     */
    String orderNo;


    /**
     * 分类code
     */
    String categoryCode;


    /**
     * 重量
     */
    Double weight;


    /**
     * 订单类型
     */
    Integer orderType;



}
