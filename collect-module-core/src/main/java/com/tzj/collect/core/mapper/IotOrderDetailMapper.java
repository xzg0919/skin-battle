package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.mysl.MyslItemBean;
import com.tzj.collect.entity.IotOrder;
import com.tzj.collect.entity.IotOrderDetail;

import java.util.List;
import java.util.Map;


public interface IotOrderDetailMapper extends BaseMapper<IotOrderDetail> {

      List<MyslItemBean> findMyslParamsByOrderId(Long orderId);

      List<Map<String,Object>> selectDetailByOrderId(Long orderId);
}
