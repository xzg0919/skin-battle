package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.business.ResultDataVParam;
import com.tzj.collect.entity.OrderItemAch;

import java.util.List;
import java.util.Map;

/**
 * @Author 王灿
 **/
public interface OrderItemAchMapper extends BaseMapper<OrderItemAch>{

    List<Map<String,Object>> selectItemSumAmount(Integer orderId);

    String orderSum(String streetId);

    List<ResultDataVParam> orderDetialNum(String streetId);

}
