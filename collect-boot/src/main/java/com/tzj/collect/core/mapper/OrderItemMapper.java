package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author 王灿
 **/
public interface OrderItemMapper extends BaseMapper<OrderItem>{

    Map<String, Object> selectItemOne(Integer orderId);

    List<Map<String,Object>> getOrderItemDetail(@Param("orderId") Long orderId,@Param("isCash") String isCash);
}
