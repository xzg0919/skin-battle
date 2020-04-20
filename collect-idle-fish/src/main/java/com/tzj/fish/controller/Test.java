package com.tzj.fish.controller;

import com.tzj.fish.common.mqtt.ConnectionOptionWrapper;
import com.tzj.fish.common.mqtt.MQTTUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class Test {

    @Autowired
    private MqttClient mqttClient;

    @RequestMapping("/send")
    public String send() throws Exception{
        final String p2pSendTopic = MQTTUtil.PARENT_TOPIC + "/p2p/" + MQTTUtil.XIANYU_CLIENTID;
        MqttMessage message = new MqttMessage();
        message.setQos(MQTTUtil.QOS_LEVEL);
        message.setPayload("龙剑是傻子".getBytes());
        /**
         *  发送普通消息时，topic 必须和接收方订阅的 topic 一致，或者符合通配符匹配规则
         */
        mqttClient.publish(p2pSendTopic, message);
       return "发送成功";
    }

}
