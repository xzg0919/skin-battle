package com.tzj.iot.common.mqtt;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020/11/20 13:58
 * @Description:
 */

public class MqttCommon {


    public enum mqttType {
        IDENTIFY_USERS(0, "识别用户");


        private int value;
        private String name;

        mqttType(final int value, final String name) {

            this.value = value;
            this.name = name;

        }

        public String getName() {
            return this.name;
        }

        public Serializable getValue() {
            return this.value;
        }

    }


    public static JSONObject convertToJson(MqttCommon.mqttType mqttType, Object   obj,String deviceCode) {
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(obj);
        jsonObject.put("mqttType", mqttType.getValue());
        jsonObject.put("deviceCode",deviceCode);
        return jsonObject;
    }


}
