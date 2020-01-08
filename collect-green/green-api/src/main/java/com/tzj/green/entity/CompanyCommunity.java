package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [社区表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_company_community")
public class CompanyCommunity extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;

    private String communityNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
    /**
     * 公司
     */
    private Long companyId;
    /**
     * 省份Id
     */
    private Long provinceId;
    /**
     * 省份
     */
    private String provinceName;
    /**
     * 城市Id
     */
    private Long cityId;
    /**
     * 所在城市
     */
    private String cityName;
    /**
     * 行政区Id
     */
    private Long areaId;
    /**
     * 行政区
     */
    private String areaName;
    /**
     * 街道Id
     */
    private Long streetId;
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
    private Long pointsNum;
    /**
     * 投放方式 0定时定点  1统一投放
     */
    private String putType;
    /**
     * 干垃圾投放时间
     */
    private String dryTime;
    /**
     * 湿垃圾投放时间
     */
    private String wetTime;
    /**
     * 湿垃圾投放时间
     */
    private String harmfulTime;
    /**
     * 可回收时间
     */
    private String recoveryTime;
    /**
     * 可回收时间星期
     */
    private String recoveryWeek;

    /**
     * 维度
     */
    private double lat;

    /**
     * 经度
     */
    private  double lng;


    @TableField(exist = false)
    private List<CommunityHouseName> communityHouseNameList;

    @TableField(exist = false)
    private String isDry;
    @TableField(exist = false)
    private String isWet;
    @TableField(exist = false)
    private String isHarmful;
    @TableField(exist = false)
    private String isRecovery;
    @TableField(exist = false)
    private List<String> dryTimeList;
    @TableField(exist = false)
    private List<String> wetTimeList;
    @TableField(exist = false)
    private List<String> harmfulTimeList;
    @TableField(exist = false)
    private List<String> recoveryTimeList;

    public String getIsDry() {
        if (StringUtils.isNotBlank(dryTime)){
            return "0";
        }else {
            return "1";
        }
    }

    public void setIsDry(String isDry) {
        this.isDry = isDry;
    }

    public String getIsWet() {
        if (StringUtils.isNotBlank(wetTime)){
            return "0";
        }else {
            return "1";
        }
    }

    public void setIsWet(String isWet) {
        this.isWet = isWet;
    }

    public String getIsHarmful() {
        if (StringUtils.isNotBlank(harmfulTime)){
            return "0";
        }else {
            return "1";
        }
    }

    public void setIsHarmful(String isHarmful) {
        this.isHarmful = isHarmful;
    }

    public String getIsRecovery() {
        if (StringUtils.isNotBlank(recoveryTime)){
            return "0";
        }else {
            return "1";
        }
    }

    public void setIsRecovery(String isRecovery) {
        this.isRecovery = isRecovery;
    }

    public List<String> getDryTimeList() {
        if (StringUtils.isNotBlank(dryTime)){
            return getTimeList(dryTime);
        }else {
            return null;
        }
    }

    public void setDryTimeList(List<String> dryTimeList) {
        this.dryTimeList = dryTimeList;
    }

    public List<String> getWetTimeList() {
        if (StringUtils.isNotBlank(wetTime)){
            return getTimeList(wetTime);
        }else {
            return null;
        }
    }

    public void setWetTimeList(List<String> wetTimeList) {
        this.wetTimeList = wetTimeList;
    }

    public List<String> getHarmfulTimeList() {
        if (StringUtils.isNotBlank(harmfulTime)){
            return getTimeList(harmfulTime);
        }else {
            return null;
        }
    }

    public void setHarmfulTimeList(List<String> harmfulTimeList) {
        this.harmfulTimeList = harmfulTimeList;
    }

    public List<String> getRecoveryTimeList() {
        if (StringUtils.isNotBlank(recoveryTime)){
            return getTimeList(recoveryTime);
        }else {
            return null;
        }
    }

    public void setRecoveryTimeList(List<String> recoveryTimeList) {
        this.recoveryTimeList = recoveryTimeList;
    }

    public static List<String> getTimeList(String time){
        if (StringUtils.isNotBlank(time)){
            String[] split = time.split(",");
            return Arrays.stream(split).collect(Collectors.toList());
        }
        return null;
    }

}
