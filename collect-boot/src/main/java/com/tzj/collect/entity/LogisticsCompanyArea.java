package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 第三方物流公司回收范围表
 */
@TableName("sb_logistics_company_area")
public class LogisticsCompanyArea extends  DataEntity<Long>{

    private Long id ;
    /**
     * 物流公司Id
     */
    private Integer logisticsId;
    /**
     * 物流公司名称
     */
    private String logisticsName;
    /**
     * 街道Id
     */
    private Integer streeId;
    /**
     * 行政区Id
     */
    private Integer areaId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Integer logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public Integer getStreeId() {
        return streeId;
    }

    public void setStreeId(Integer streeId) {
        this.streeId = streeId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
}
