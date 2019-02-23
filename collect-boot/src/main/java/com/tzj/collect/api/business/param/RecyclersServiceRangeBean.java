package com.tzj.collect.api.business.param;

import com.tzj.collect.api.ali.param.AreaBean;

import java.util.List;
import java.util.Map;

public class RecyclersServiceRangeBean {
    /**
     * 0是经理，1下级回收人员，2拒绝该用户
     */
    private String isEnable;
    /**
     * 市级Id
     */
    private String cityId;
    /**
     * 区域Ids
     */
    private String areaIds;
    /**
     * 区域Id
     */
    private String areaId;
    /**
     * 街道Id
     */
    private String streeId;
    /**
     * 区域经理Id
     */
    private String managerId;
    /**
     * 回收人员Id
     */
    private String recycleId;
    /**
     * 回收人员姓名
     */
    private String recycleName;
    /**
     * 当前展示的条数
     */
    private Integer pageSize=20;
    /**
     * 当前页数
     */
    private Integer pageNum=1;
    /**
     * 区域list
     */
    private List<AreaBean> areaList;

    /**
     * 回收类型list
     */
    private List<TitleBean> TitleList;

    public List<TitleBean> getTitleList() {
        return TitleList;
    }

    public void setTitleList(List<TitleBean> titleList) {
        TitleList = titleList;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getStreeId() {
        return streeId;
    }

    public void setStreeId(String streeId) {
        this.streeId = streeId;
    }

    public List<AreaBean> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaBean> areaList) {
        this.areaList = areaList;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    public String getRecycleName() {
        return recycleName;
    }

    public void setRecycleName(String recycleName) {
        this.recycleName = recycleName;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public String getRecycleId() {
        return recycleId;
    }

    public void setRecycleId(String recycleId) {
        this.recycleId = recycleId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
}
