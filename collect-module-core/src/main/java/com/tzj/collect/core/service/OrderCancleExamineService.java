package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.entity.OrderCancleExamine;

public interface OrderCancleExamineService extends IService<OrderCancleExamine> {

    Object getCancleOrderDetail(OrderBean orderbean);

}
