package com.tzj.collect.core.handler;


import com.tzj.collect.entity.Order;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/10/15 14:06
 * @Description:
 */

public abstract  class OrderHandler {


    /**
     * 完成订单前执行
     */
    public   abstract void beforeComplete(Long recyclerId, Order.TitleType titleType,String aliUserId);


}
