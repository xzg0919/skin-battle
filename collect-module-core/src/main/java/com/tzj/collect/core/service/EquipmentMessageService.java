package com.tzj.collect.core.service;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface EquipmentMessageService {
    void dealWithMessage(String topic, String message);

    void sendMessageToMQ4IoTUseSignatureMode(String message, String sendTo) throws MqttException;

    void sendMessageToMQ4IoTUseTokenMode(String message, String sendTo) throws MqttException;
}
