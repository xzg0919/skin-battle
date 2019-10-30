package com.tzj.collect.common.constant;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
public class ApplicaInit {

    private String isMysl;

    private String isPayment;

    private  Boolean isOpenTransferThread;//是否开启转账线程

}
