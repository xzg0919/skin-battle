package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.entity.PashmOrder;


public interface PashmOrderService extends IService<PashmOrder>{
	PashmOrder selectByOrderNo(String orderNo);
}
