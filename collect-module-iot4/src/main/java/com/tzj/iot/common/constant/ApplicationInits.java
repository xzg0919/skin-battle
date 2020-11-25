package com.tzj.iot.common.constant;

import com.tzj.collect.common.constant.ApplicaInit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "application-init")
public class ApplicationInits {


    @Value("${isMysl}")
    private String isMysl;

    @Value("${isOpenTransferThread}")
    private Boolean isOpenTransferThread;

    @Value("${notifyUrl}")
    private String notifyUrl;

    @Value("${stationUrl}")
    private String stationUrl;

    @Bean
    public ApplicaInit applicaInitConfig() {
        ApplicaInit applicaInit  = new ApplicaInit();
        applicaInit.setIsMysl(isMysl);
        applicaInit.setNotifyUrl(notifyUrl);
        applicaInit.setStationUrl(stationUrl);
        applicaInit.setIsOpenTransferThread(isOpenTransferThread);
        return applicaInit;

    }
}
