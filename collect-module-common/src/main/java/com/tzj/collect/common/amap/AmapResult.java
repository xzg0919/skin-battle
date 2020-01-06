package com.tzj.collect.common.amap;


import lombok.Data;

import java.io.Serializable;

@Data
public class AmapResult implements Serializable {

    private static final long serialVersionUID = 7402296571653735491L;


    private String name; // 名称

    private String neighborhood;

    private String formattedAd;


    private String address; //地址


    private String location;    //经纬度


    private String province;    //省


    private String city;    //城市


    private String district;    //区县


    private String township;    //街道、镇


    private String citycode;    //城市编号


    private String adcode;  //区县编号


    private String towncode;    //街道、镇编号

}
