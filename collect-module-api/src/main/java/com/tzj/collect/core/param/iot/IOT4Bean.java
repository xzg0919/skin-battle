package com.tzj.collect.core.param.iot;

import lombok.Data;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020/11/20 10:39
 * @Description:
 */
@Data
public class IOT4Bean {


    /**
     * 设备号
     */
    String deviceCode;

    /**
     * 时间戳
     */
    Long timeStamp;


    /**
     * MD5加密参数
     */
    String sign;


    String aliUserId;
}
