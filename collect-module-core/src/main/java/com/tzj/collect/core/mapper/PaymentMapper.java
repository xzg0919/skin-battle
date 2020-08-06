package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.Payment;
import org.apache.ibatis.annotations.Param;

public interface PaymentMapper extends BaseMapper<Payment> {

    Payment selectPayOneMinByOrderSn(@Param("orderNo") String orderNo);
}
