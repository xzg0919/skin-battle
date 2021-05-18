package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.PrepayOrderMapper;
import com.tzj.collect.core.service.PrepayOrderService;
import com.tzj.collect.entity.PrepayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrepayOrderServiceImpl extends ServiceImpl<PrepayOrderMapper, PrepayOrder> implements PrepayOrderService {

    @Autowired
    PrepayOrderMapper prepayOrderMapper;



    @Override
    public PrepayOrder findByOutTradeNo(String outTradeNo) {
        return selectOne(new EntityWrapper<PrepayOrder>().eq("out_trade_no",outTradeNo));
    }


}
