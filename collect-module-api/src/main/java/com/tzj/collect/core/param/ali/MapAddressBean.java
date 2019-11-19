package com.tzj.collect.core.param.ali;

import lombok.Data;

@Data
public class MapAddressBean {

    private String id;
    /**
     *330103
     */
    private String adcCode;
    /**
     * 浙江省杭州市下城区朝晖街道环球中心西湖文化广场
     */
    private String address;
    /**
     * 杭州市
     */
    private String city;
    /**
     * 0571
     */
    private String cityCode;
    /**
     * 下城区
     */
    private String district;
    /**
     * 121.451897,31.229806
     */
    private String location;
    /**
     * 环球中心
     */
    private String name;
    /**
     * 浙江省
     */
    private String province;
    /**
     * 330103006000
     */
    private String townCode;
    /**
     * 朝晖街道
     */
    private String townShip;
    /**
     * 是否是默认
     */
    private String isSelected = "0";

    private String tel;

    private String userName;

    private String houseNumber;

    /**
     * 用户保存地图获取名字
     */
    private String mapName;
    /**
     * 用户点击地图地址
     */
    private String mapAddress;






}
