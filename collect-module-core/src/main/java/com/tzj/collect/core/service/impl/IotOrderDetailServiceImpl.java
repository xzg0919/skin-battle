package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.IotOrderDetailMapper;
import com.tzj.collect.core.mapper.IotOrderMapper;
import com.tzj.collect.core.param.mysl.MyslItemBean;
import com.tzj.collect.core.service.IotOrderDetailService;
import com.tzj.collect.core.service.IotOrderService;
import com.tzj.collect.entity.IotOrder;
import com.tzj.collect.entity.IotOrderDetail;
import net.sf.ehcache.search.expression.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class IotOrderDetailServiceImpl extends ServiceImpl<IotOrderDetailMapper, IotOrderDetail> implements IotOrderDetailService {

    @Autowired
    private IotOrderDetailMapper iotOrderDetailMapper;


    @Override
    public List<IotOrderDetail> selectByOrderId(Long orderId) {
        return this.selectList(new EntityWrapper<IotOrderDetail>().eq("del_flag",0)
        .eq("order_id",orderId));
    }



    @Override
    public List<MyslItemBean> findMyslParamsByOrderId(Long orderId) {
        return iotOrderDetailMapper.findMyslParamsByOrderId(orderId);
    }

    @Override
    public IotOrderDetail selectByOrderIdAndCategoryId(Long orderId, Long categoryId) {
        return this.selectOne(new EntityWrapper<IotOrderDetail>().eq("del_flag",0)
                .eq("order_id",orderId).eq("category_id",categoryId));
    }

    @Override
    public List<Map<String, Object>> selectDetailByOrderId(Long orderId) {
        return iotOrderDetailMapper.selectDetailByOrderId(orderId);
    }


}
