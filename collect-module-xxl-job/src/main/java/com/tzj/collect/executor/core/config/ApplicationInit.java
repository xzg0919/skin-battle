package com.tzj.collect.executor.core.config;


import com.tzj.collect.common.constant.ApplicaInit;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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


    @Bean
    public ApplicaInit applicaInitConfig() {
        ApplicaInit applicaInit  = new ApplicaInit();
        applicaInit.setIsMysl(isMysl);
        applicaInit.setNotifyUrl(null);
        applicaInit.setStationUrl(null);
        applicaInit.setIsOpenXyConsumer(null);
        applicaInit.setIsOpenTransferThread(isOpenTransferThread);
        applicaInit.setAuthToken(null);
        return applicaInit;

    }

}
