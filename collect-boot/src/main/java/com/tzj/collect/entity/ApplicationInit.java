package com.tzj.collect.entity;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-init.yml")
public class ApplicationInit {

    @Value("${isMysl}")
    private String isMysl;

    public String getIsMysl() {
        return isMysl;
    }

    public void setIsMysl(String isMysl) {
        this.isMysl = isMysl;
    }
}
