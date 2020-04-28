package com.tzj.collect.common.constant;

import lombok.Data;

@Data
public class ApplicaInit {

    private String isMysl;

    private String notifyUrl;

    private String stationUrl;

    private Boolean isOpenXyConsumer;//是否开启咸鱼订单消费

    private Boolean isOpenTransferThread;//是否开启转账线程

    private String authToken;

}
