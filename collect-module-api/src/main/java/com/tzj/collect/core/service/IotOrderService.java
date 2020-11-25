package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.IotCategory;
import com.tzj.collect.entity.IotOrder;

import java.util.Map;


public interface IotOrderService extends IService<IotOrder>{


    /**
     * 根据订单号查找进行中的订单
     * @param orderNo
     * @return
     */
    IotOrder selectOrderByOrderNo(String orderNo,IotOrder.OrderStatus orderStatus);


    Map<String,Object> findOrderListByUserId(String aliUserId,  int num, int size);

    Object getOrderDetail(String orderNo);
}
