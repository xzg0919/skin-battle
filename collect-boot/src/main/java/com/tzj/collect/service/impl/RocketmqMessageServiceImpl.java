package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.RocketmqMessage;
import com.tzj.collect.mapper.RocketmqMessageMapper;
import com.tzj.collect.service.RocketmqMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class RocketmqMessageServiceImpl extends ServiceImpl<RocketmqMessageMapper,RocketmqMessage> implements RocketmqMessageService{
        @Autowired
        private RocketmqMessageMapper RocketmqMessageMapper;




}
