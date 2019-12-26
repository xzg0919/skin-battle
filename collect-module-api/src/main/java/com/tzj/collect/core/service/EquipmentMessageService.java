package com.tzj.collect.core.service;

import com.tzj.collect.core.param.iot.EquipmentParamBean;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Map;

public interface EquipmentMessageService {
    void dealWithMessage(String topic, String message, MqttClient mqttClient);

    void sendMessageToMQ4IoTUseSignatureMode(String message, String sendTo, MqttClient mqttClient) throws MqttException;

    void sendMessageToMQ4IoTUseTokenMode(String message, String sendTo, MqttClient mqttClient) throws MqttException;

    Boolean redisSetAdd(String key, String value);

    Boolean redisSetCheck(String key, String value);

    Map<String, Object> equipmentCodeOpen(EquipmentParamBean equipmentParamBean, MqttClient mqttClient);
}
