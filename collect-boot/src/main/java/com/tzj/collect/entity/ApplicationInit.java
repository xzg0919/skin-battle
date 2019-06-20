package com.tzj.collect.entity;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-init.yml")
public class ApplicationInit {

    @Value("${isMysl}")
    private String isMysl;
    @Value("${isDD}")
    private String isDd;

    public String getIsMysl() {
        return isMysl;
    }

    public void setIsMysl(String isMysl) {
        this.isMysl = isMysl;
    }

    public String getIsDd() {
        return isDd;
    }

    public void setIsDd(String isDd) {
        this.isDd = isDd;
    }
}
