package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.mapper.OrderCancleExamineMapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.OrderCancleExamineService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderCancleExamine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional(readOnly=true)
public class OrderCancleExamineServiceImpl extends ServiceImpl<OrderCancleExamineMapper, OrderCancleExamine> implements OrderCancleExamineService {

    @Autowired
    public OrderService orderService;

    public Object getCancleOrderDetail(OrderBean orderbean){
        List<OrderCancleExamine> orderCancleExamineList = this.selectList(new EntityWrapper<OrderCancleExamine>().eq("order_no", orderbean.getOrderNo()));
        Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", orderbean.getOrderNo()));
        if (null==orderCancleExamineList){
            orderCancleExamineList = new ArrayList<>();
            OrderCancleExamine orderCancleExamine = new OrderCancleExamine();
            Date updateDate = ToolUtils.getDateTime(order.getCancelTime()+"");
            orderCancleExamine.setUpdateDate(updateDate);
        }else {
            orderCancleExamineList.stream().forEach(OrderCancleExamine->{
                OrderCancleExamine.setOrder(order);
            });
        }
        return orderCancleExamineList;
    }

}
