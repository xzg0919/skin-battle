package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.business.PashmBean;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.entity.PashmOrder;

import java.util.HashMap;
import java.util.Map;


public interface PashmOrderService extends IService<PashmOrder>{
	PashmOrder selectByOrderNo(String orderNo);

	HashMap<String,Object> savePashmOrder(PashmBean pashmBean);
}
