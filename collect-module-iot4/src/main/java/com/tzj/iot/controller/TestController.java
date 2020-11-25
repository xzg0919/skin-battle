package com.tzj.iot.controller;

import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.api.commom.constant.MQTTConst;
import com.tzj.module.easyopen.exception.ApiException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020/11/20 13:29
 * @Description:
 */
@RestController
@RequestMapping("iot4")
public class TestController {

    @Autowired
    MqttClient mqttClient;


    @RequestMapping("sendMessage")
    public void sendMessage(){

        //发送用户信息
        MqttMessage mqttMessage = new MqttMessage("{\"user\":\"213123\"}".getBytes());
        mqttMessage.setQos(1);
        try {
            mqttClient.publish(MQTTConst.PARENT_TOPIC + "/" + "iot4", mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
            throw new ApiException("箱门开启失败，请稍后再试！");
        }
    }
}
