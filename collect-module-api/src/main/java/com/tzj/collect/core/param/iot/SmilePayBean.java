package com.tzj.collect.core.param.iot;

import lombok.Data;

/**
 * 刷脸bean
 * @author sgmark
 * @create 2019-11-26 10:31
 **/
@Data
public class SmilePayBean {

    private String apdidToken;

    private String appName;

    private String appVersion;
    /**
     * 生物识别元信息
     */
    private String bioMetaInfo;

    private String fToken;
    /**
     * 所要登录的设备编号的硬件编号
     */
    private String hardwareCode;
    /**
     * 识别信息
     */
    private String metaInfo;
}
