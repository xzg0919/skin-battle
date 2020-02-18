package com.tzj.fish.service.impl;

import com.tzj.collect.api.commom.constant.MQTTConst;
import com.tzj.fish.service.OnMessageService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 消息到达实现
 *
 * @author sgmark
 * @create 2020-02-14 10:33
 **/
@Component
@Service
public class OnMessageServiceImpl implements OnMessageService {

    final int qosLevel = 1;
    @Resource
    private MqttClient mqttClient;

    @Override
    public void onMessage(Map<String, Object> content) throws MqttException {
        //根据地址去高德反查街道
        //发送mqtt消息到收呗服务
        sendMessageToMQ4IoTUseSignatureMode("", "admin_collect", mqttClient);
    }
    public void sendMessageToMQ4IoTUseSignatureMode(String message, String topic, MqttClient mqttClient) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(qosLevel);
        System.out.println("message:"+message);
        mqttClient.publish(MQTTConst.PARENT_TOPIC + "/" + topic, mqttMessage);
    }
}
