package com.tzj.iot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-init.yml")
@Data
public class ApplicationInit {

    @Value("${isMysl}")
    private String isMysl;
    @Value("${isDd}")
    private String isDd;
    @Value("${isWuGongJin}")
    private String isWuGongJin;
    @Value("${isOpenTransferThread}")
    private Boolean isOpenTransferThread;

}
