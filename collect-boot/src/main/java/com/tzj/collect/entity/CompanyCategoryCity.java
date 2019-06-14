package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;

/**
 *
 *回收企业城市分类关联表
 *
 * @Author 王灿
 **/
@TableName("sb_company_category_city")
public class CompanyCategoryCity extends  DataEntity<Long> {
    private Long id;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 城市Id
     */
    private String cityId;
    /**
     * 分类Id
     */
    private Integer categoryId;
    /**
     * 父级编号
     */
    private Integer parentId;
    /**
     * 父级名称
     */
    private String parentName;
    /**
     * 所有父级编号
     */
    private String parentIds;
    /**
     * 回收单价
     */
    private BigDecimal price;
    /**
     * 计量单位
     */
    private String unit;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
