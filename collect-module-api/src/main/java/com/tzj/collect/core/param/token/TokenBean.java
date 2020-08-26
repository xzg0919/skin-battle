package com.tzj.collect.core.param.token;


import com.tzj.collect.entity.CompanyEquipment;
import lombok.Data;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Data
public class TokenBean {

    private String token;

    private long expire;

    private String signKey;

    private String blueTooth;

    private String isAuth;//是否授权，0授权了，1没有授权

    private CompanyEquipment companyEquipment;//是否授权，0授权了，1没有授权

}
