package com.tzj.collect.common.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;



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

    @Value("${isOpenXyConsumer}")
    private Boolean isOpenXyConsumer;

    @Value("${authToken}")
    private String authToken;

    @Bean
    public ApplicaInit applicaInitConfig() {
        ApplicaInit applicaInit  = new ApplicaInit();
        applicaInit.setIsMysl(isMysl);
        applicaInit.setNotifyUrl(notifyUrl);
        applicaInit.setStationUrl(stationUrl);
        applicaInit.setIsOpenXyConsumer(isOpenXyConsumer);
        applicaInit.setIsOpenTransferThread(isOpenTransferThread);
        applicaInit.setAuthToken(authToken);
        return applicaInit;

    }


}
