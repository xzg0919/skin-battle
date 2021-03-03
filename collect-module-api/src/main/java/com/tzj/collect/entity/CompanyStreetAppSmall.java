package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 小家电物流企业关联街道表
 * @author wangcan
 *
 */
@TableName("sb_company_street_app_small")
public class CompanyStreetAppSmall extends DataEntity<Long>{
        private Long id;
    /**
     * 公司Id
     */
    private Integer companyId;
    /**
     * 街道Id
     */
    private Integer streetId;
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

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
}
