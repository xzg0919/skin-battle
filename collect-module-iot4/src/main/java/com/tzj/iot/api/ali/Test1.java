package com.tzj.iot.api.ali;

import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.api.commom.constant.MQTTConst;
import com.tzj.collect.api.commom.mqtt.util.ConnectionOptionWrapper;
import com.tzj.iot.common.mqtt.MqttCommon;
import lombok.SneakyThrows;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static com.tzj.collect.api.commom.constant.MQTTConst.*;
import static com.tzj.collect.api.commom.constant.MQTTConst.SECRET_KEY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/3/29 15:14
 * @Description:
 */
public class Test1 {


    public static void main(String[] args) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("ssss","test");
        MqttMessage mqttMessage = new MqttMessage(MqttCommon.convertToJson(MqttCommon.mqttType.IDENTIFY_USERS, jsonObject, "7f786a73").toString().getBytes());
        sendMqttMessage(mqttMessage, "GID-IOT-MQTT" + "@@@7f786a73");

    }


    @SneakyThrows
    public  static void  sendMqttMessage(MqttMessage message, String clientId){
        String topic        = MQTTConst.PARENT_TOPIC + "/" + "iot4";
        int qos             = 0;
        String broker       = "tcp://" + END_POINT + ":1883";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(INSTANCE_ID, ACCESS_KEY, SECRET_KEY, clientId);
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            mqttClient.setTimeToWait(5000);
            mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
            message.setQos(qos);
            mqttClient.publish(topic, message);
        } catch(MqttException me) {
            me.printStackTrace();
        }
    }
}
