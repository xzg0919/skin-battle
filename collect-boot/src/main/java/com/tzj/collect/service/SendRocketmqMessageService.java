package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.SendRocketmqMessage;

/**
 *
 */
public interface SendRocketmqMessageService extends IService<SendRocketmqMessage> {

    void sendDeliveryOrder (String param,String topicName);




}
