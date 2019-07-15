package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.OrderItem;

import java.util.Map;

/**
 * @Author 王灿
 **/
public interface OrderItemMapper extends BaseMapper<OrderItem>{

    Map<String, Object> selectItemOne(Integer orderId);

}
