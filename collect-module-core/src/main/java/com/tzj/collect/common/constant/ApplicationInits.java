package com.tzj.collect.common.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;



@Configuration
public class ApplicationInits {


    @Value("${isMysl}")
    private String isMysl;

    @Value("${isTestPayment}")
    private String isTestPayment;

    @Bean
    public ApplicaInit applicaInitConfig() {
        ApplicaInit applicaInit  = new ApplicaInit();
        System.out.println(isMysl+"------------------------------------------"+isTestPayment);
        applicaInit.setIsMysl(isMysl);
        applicaInit.setIsTestPayment(isTestPayment);
        return applicaInit;

    }


}
