package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RocketmqMessageMapper;
import com.tzj.collect.core.service.RocketmqMessageService;
import com.tzj.collect.entity.RocketmqMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class RocketmqMessageServiceImpl extends ServiceImpl<com.tzj.collect.core.mapper.RocketmqMessageMapper,RocketmqMessage> implements RocketmqMessageService {
        @Autowired
        private RocketmqMessageMapper RocketmqMessageMapper;




}
