package com.tzj.collect.common.constant;

import lombok.Data;

@Data
public class ApplicaInit {

    private String isMysl;

    private String isPayment;

    private Boolean isOpenTransferThread;//是否开启转账线程

}
