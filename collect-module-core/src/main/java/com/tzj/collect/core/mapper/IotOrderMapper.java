package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.IotOrder;
import com.tzj.collect.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IotOrderMapper extends BaseMapper<IotOrder> {

    /**
     * 获取会员的订单列表 分页
     * @author 王灿
     * @param sizeStart : 第几条开始
     * @param size : 共多少条
     * @return
     */
    List<IotOrder> getOrderlist(@Param("aliUserId")String aliUserId, @Param("status") int status,  @Param("sizeStart") int sizeStart, @Param("size") int size);

}
