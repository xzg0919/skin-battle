package com.tzj.collect.api.iot.messagecode;

import com.baomidou.mybatisplus.enums.IEnum;

/**
 * iot错误信息
 *
 * @author sgmark
 * @create 2019-04-23 10:36
 **/
public enum  MessageCode implements IEnum{
        SUCCESS_OPEN("200", "请您投放"),   	 //操作成功
        EMPLOY_ERROR("201", "此设备使用中"),   	 //占用中
        STOPPAGE_ERROR("202", "柜门未打开"),   	 //设备故障
        //其他类型(OTHER)
        OTHERS_ERROR("203", "柜门未打开"),       //设备故障

        ERROR_QRCODE("404", "二维码未识别");
        private String key;

        private String value;

        MessageCode(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public static String getValueByKey(String key) {
            MessageCode[] enums = MessageCode.values();
            for (int i = 0; i < enums.length; i++) {
                if (enums[i].getKey().equals(key)) {
                    return enums[i].getValue();
                }
            }
            return "";
        }
}
