package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.api.commom.mqtt.MQTTConfig;
import com.tzj.collect.api.commom.mqtt.util.ConnectionOptionWrapper;
import com.tzj.collect.core.service.EquipmentMessageService;
import com.tzj.collect.entity.CompanyEquipment;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author sgmark
 * @create 2019-11-15 9:50
 **/
@Service
@Transactional(readOnly = true)
public class EquipmentMessageServiceImpl implements EquipmentMessageService {

    final int qosLevel = 0;
    @Resource(name = "connectionOptionWrapperSignature")
    private ConnectionOptionWrapper connectionOptionWrapper;
    //    @Value("${mqtt-config.instanceId}")
//    private String  instanceId;
//    @Value("${mqtt-config.clientId}")
//    private String clientId;
//    @Value("${mqtt-config.parentTopic}")
//    private String parentTopic;
    @Resource
    private MQTTConfig mqttConfig;


    @Override
    public void dealWithMessage(String topic,String message) {
        try {
            Map<String, Object> messageMap = JSONObject.parseObject(message);
            //关闭箱门
            if(CompanyEquipment.EquipmentAction.EquipmentActionCode.EQUIPMENT_CLOSE.getKey().equals(messageMap.get("code"))){
                //拿到物品识别图片，保存至oss,返回识别结果(挡板翻转)
                messageMap.get("imgUrl");
                //调用阿里的物品识别接口 todo

            }else if (CompanyEquipment.EquipmentAction.EquipmentActionCode.RECYCLE_CLOSE.getKey().equals(messageMap.get("code"))){
                //清运关门

            }
        }catch (Exception e){
            //消息体错误
        }

    }

    @Override
    public void sendMessageToMQ4IoTUseSignatureMode(String message, String sendTo) throws MqttException {

    }

    @Override
    public void sendMessageToMQ4IoTUseTokenMode(String message, String sendTo) throws MqttException {

    }
}
