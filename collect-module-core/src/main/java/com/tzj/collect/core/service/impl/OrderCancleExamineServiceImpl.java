package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.OrderCancleExamineMapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.OrderCancleExamineService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderCancleExamine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional(readOnly=true)
public class OrderCancleExamineServiceImpl extends ServiceImpl<OrderCancleExamineMapper, OrderCancleExamine> implements OrderCancleExamineService {

    @Autowired
    public OrderService orderService;

    public Object getCancleOrderDetail(OrderBean orderbean){
        Map<String,Object> resultMap = new HashMap<>();
        List<OrderCancleExamine> orderCancleExamineList = this.selectList(new EntityWrapper<OrderCancleExamine>().eq("order_no", orderbean.getOrderNo()));
        Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", orderbean.getOrderNo()));
        resultMap.put("orderCancleExamineList",orderCancleExamineList);
        resultMap.put("order",order);
        return resultMap;
    }

}
