package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;


/**
 *
 * 回收人员电器服务范围表
 *
 * @Author 王灿
 **/
@TableName("sb_recyclers_range_appliance")
public class RecyclersRangeAppliance<Data> extends DataEntity<Long> {
    private Long id;
    /**
     * 回收经理Id
     */
    private Integer recyclersId;
    /**
     * 所属的企业Id
     */
    private Integer companyId;
    /**
     * 所属的区域id目前到街道
     */
    private Integer streetId;
    /**
     * 所属区域的父亲节点
     */
    private String areaId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRecyclersId() {
        return recyclersId;
    }

    public void setRecyclersId(Integer recyclersId) {
        this.recyclersId = recyclersId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getStreetId() {
        return streetId;
    }

    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
