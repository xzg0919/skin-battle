package com.tzj.collect.executor.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/5/17 11:30
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "alipay")
public class AliPayProperties {

    private String publicCert;

    private String rootCert;

    private String appCert;
}
