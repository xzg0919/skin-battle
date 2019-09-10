package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.OrderCancleExamineMapper;
import com.tzj.collect.core.service.OrderCancleExamineService;
import com.tzj.collect.entity.OrderCancleExamine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class OrderCancleExamineServiceImpl extends ServiceImpl<OrderCancleExamineMapper, OrderCancleExamine> implements OrderCancleExamineService {


}
