package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.experimental.Accessors;

/**
 * 物流企业关联街道表
 * @author wangcan
 *
 */
@Accessors(chain = true)
@TableName("sb_company_stree")
public class CompanyStree extends DataEntity<Long>{
        private Long id;
    /**
     * 公司Id
     */
    private Integer companyId;
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
