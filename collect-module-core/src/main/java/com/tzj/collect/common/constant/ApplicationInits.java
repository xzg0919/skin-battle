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

    @Value("${isPayment}")
    private String isPayment;

    @Value("${isOpenTransferThread}")
    private Boolean isOpenTransferThread;

    @Bean
    public ApplicaInit applicaInitConfig() {
        ApplicaInit applicaInit  = new ApplicaInit();
        applicaInit.setIsMysl(isMysl);
        applicaInit.setIsPayment(isPayment);
        applicaInit.setIsOpenTransferThread(isOpenTransferThread);
        return applicaInit;

    }


}
