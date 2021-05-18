package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.PrepayOrder;

public interface PrepayOrderService extends IService<PrepayOrder>{

   PrepayOrder findByOutTradeNo(String outTradeNo);

}
