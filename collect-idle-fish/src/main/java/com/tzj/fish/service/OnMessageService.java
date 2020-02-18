package com.tzj.fish.service;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Map;

/**
 * 消息到达
 *
 * @author sgmark
 * @create 2020-02-14 10:31
 **/
public interface OnMessageService {
    void onMessage(Map<String, Object> content) throws MqttException;
}
