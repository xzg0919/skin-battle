package com.tzj.collect.api.ali.result;

import com.tzj.module.easyopen.doc.annotation.ApiDocField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AmapResult implements Serializable {

    private static final long serialVersionUID = 7402296571653735491L;

    @ApiDocField(description = "名称")
    private String name; // 名称

    private String neighborhood;

    private String formattedAd;

    @ApiDocField(description = "地址")
    private String address; //地址

    @ApiDocField(description = "经纬度")
    private String location;    //经纬度

    @ApiDocField(description = "省")
    private String province;    //省

    @ApiDocField(description = "城市")
    private String city;    //城市

    @ApiDocField(description = "区县")
    private String district;    //区县

    @ApiDocField(description = "街道、镇")
    private String township;    //街道、镇

    @ApiDocField(description = "城市编号")
    private String citycode;    //城市编号

    @ApiDocField(description = "区县编号")
    private String adcode;  //区县编号

    @ApiDocField(description = "街道、镇编号")
    private String towncode;    //街道、镇编号

}
