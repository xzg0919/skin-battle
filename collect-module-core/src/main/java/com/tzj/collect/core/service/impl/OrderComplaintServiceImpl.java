package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.OrderComplaintMapper;
import com.tzj.collect.core.service.OrderComplaintService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderComplaint;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class OrderComplaintServiceImpl extends ServiceImpl<OrderComplaintMapper, OrderComplaint> implements OrderComplaintService {

    @Autowired
    private OrderComplaintMapper orderComplaintMapper;
    @Autowired
    private OrderService orderService;

    @Override
    public Object getIsOrderComplaint(String orderNo) {
        Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", orderNo));
        if (null==order){
            throw new ApiException("传入的订单Id有误");
        }
        List<OrderComplaint> orderComplaintList = new ArrayList<>();
        orderComplaintList = orderComplaintMapper.selectComplaintList(orderNo);
        List<Map<String, Object>> returnMap = new ArrayList<>();
        if(orderComplaintList.size()>0){
            for(OrderComplaint list :orderComplaintList){
                Map<String, Object> map = new HashMap<>();
              if("0".equals(list.getType())){
                  map.put("催派",null ==list.getComplaintBack()?"未反馈":"已反馈");
                  returnMap.add(map);
              }else if("1".equals(list.getType())){
                    map.put("催接",null ==list.getComplaintBack()?"未反馈":"已反馈");
                    returnMap.add(map);
                }else if("2".equals(list.getType())){
                  map.put("催收",null ==list.getComplaintBack()?"未反馈":"已反馈");
                  returnMap.add(map);
              }
            }
        }
        return returnMap;
    }
}
