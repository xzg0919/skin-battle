package com.tzj.green.param;


import com.tzj.green.entity.CommunityHouseName;
import lombok.Data;

import java.util.List;

@Data
public class CompanyCommunityBean {

    private String id;
    /**
     * 公司
     */
    private String companyId;
    /**
     * 省份Id
     */
    private String provinceId;
    /**
     * 省份
     */
    private String provinceName;
    /**
     * 城市Id
     */
    private String cityId;
    /**
     * 所在城市
     */
    private String cityName;
    /**
     * 行政区Id
     */
    private String areaId;
    /**
     * 行政区
     */
    private String areaName;
    /**
     * 街道Id
     */
    private String streetId;
    /**
     * 街道
     */
    private String streetName;
    /**
     * 居委
     */
    private String communityName;
    /**
     * 小区数量
     */
    private String houseNum;
    /**
     * 积分次数限制
     */
    private String pointsNum;
    /**
     * 投放方式 0定时定点  1统一投放
     */
    private String putType;
    /**
     * 是否打开干垃圾投放
     */
    private String isDry;
    /**
     * 干垃圾投放时间
     */
    private String dryTime;
    /**
     * 是否打开湿垃圾投放
     */
    private String isWet;
    /**
     * 湿垃圾投放时间
     */
    private String wetTime;
    /**
     * 是否打开有害圾投放
     */
    private String isHarmful;
    /**
     * 有害圾投放时间
     */
    private String harmfulTime;
    /**
     * 是否打开可回收物
     */
    private String isRecovery;
    /**
     * 可回收时间
     */
    private String recoveryTime;
    /**
     * 可回收时间星期
     */
    private String recoveryWeek;

    private List<CommunityHouseName> houseNameList;

    private PageBean pageBean;

    private String communityHouseName;

    private String communityHouseId;


}
