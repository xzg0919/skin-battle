package com.tzj.collect.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * wxpay pay properties.
 *
 * @author Binary Wang
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
    /**
     * 商户  小程序appid
     */
    private String appIdWx;

    /**
     * 商户app appid
     */
    private String appIdApp;


    /**
     * 商户key
     */
    private String mchKey;


    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    private String keyPath;


    /**
     * 服务商商户号
     */
    private String mchId;


}

