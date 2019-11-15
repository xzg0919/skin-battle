package com.tzj.collect.core.param.iot;

import lombok.Data;

/**
 * @author sgmark
 * @create 2019-11-13 9:43
 **/
@Data
public class EquipmentParamBean {

    private String hardwareCode;//所要登录的设备编号的硬件编号

    private String  captcha;//登录验证码，若设备已激活，只需要设备编号获取token
}
