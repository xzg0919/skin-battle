package com.tzj.collect.module.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/5/17 11:30
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "alipay")
class AliPayProperties {

    private String publicCert;

    private String rootCert;

    private String appCert;
}
