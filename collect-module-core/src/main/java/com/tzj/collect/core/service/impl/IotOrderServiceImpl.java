package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.IotOrderMapper;
import com.tzj.collect.core.service.IotOrderDetailService;
import com.tzj.collect.core.service.IotOrderService;
import com.tzj.collect.entity.IotOrder;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class IotOrderServiceImpl extends ServiceImpl<IotOrderMapper, IotOrder> implements IotOrderService {

    @Autowired
    private IotOrderMapper iotOrderMapper;
    @Autowired
    private IotOrderDetailService iotOrderDetailService;


    @Override
    public IotOrder selectOrderByOrderNo(String orderNo,IotOrder.OrderStatus orderStatus) {
        return this.selectOne(new EntityWrapper<IotOrder>().eq("order_no",orderNo).eq("del_flag","0")
        .eq("status_",orderStatus.getValue()));
    }

    @Override
    public Map<String, Object> findOrderListByUserId(String aliUserId, int num, int size) {
        EntityWrapper<IotOrder> wrapper = new EntityWrapper<IotOrder>();
        wrapper.eq("ali_user_id", aliUserId);
        wrapper.eq("del_flag", "0");
        wrapper.orderBy("create_date", false);
        wrapper.eq("status_", "1");
        int i = this.selectCount(wrapper);
        Map<String, Object> map = new HashMap<String, Object>();
        List<IotOrder> listOrder = iotOrderMapper.getOrderlist(aliUserId, (Integer) IotOrder.OrderStatus.COMPLETE.getValue(), (num - 1) * size, size);
        map.put("pageNum", num);
        map.put("count", i);
        map.put("listOrder", listOrder);
        return map;
    }

    @Override
    public Object getOrderDetail(String orderNo) {
        HashMap<String,Object> result=new HashMap<>();
        IotOrder iotOrder = this.selectOrderByOrderNo(orderNo,IotOrder.OrderStatus.COMPLETE);
        if(iotOrder ==null){
            throw new ApiException("订单不存在！");
        }
        List<Map<String, Object>> details = iotOrderDetailService.selectDetailByOrderId(iotOrder.getId());
        result.put("order",iotOrder);
        result.put("orderDetail",details);
        return result;
    }


}
