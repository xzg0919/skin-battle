package com.tzj.fish.controller;


import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.tzj.fish.common.rocket.RocketUtil;
import com.tzj.module.api.annotation.AuthIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rocket")
public class RocketController {

    @Autowired
    private Producer producer;


    @RequestMapping("/getSend")
    public Object getSend(){

        Message message = new Message(RocketUtil.ALI_TOPIC,"TagA","Hello MQ".getBytes());

        message.setKey("我是订单信息");

        // 发送消息，只要不抛异常就是成功
        SendResult sendResult = producer.send(message);
        //打印 Message ID，以便用于消息发送状态查询
        System.out.println("Send Message success. Message ID is: " + sendResult.getMessageId());

        return "发送消息了";
    }

}
