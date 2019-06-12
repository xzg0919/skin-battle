package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;

/**
 *
 *公司城市分类属性价格增减表
 *
 * @Author 王灿
 **/
@TableName("sb_company_category_attr_option_city")
public class CompanyCategoryAttrOptionCity extends  DataEntity<Long> {
    private Long id;
    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 城市Id
     */
    private String cityId;
    /**
     * 分类属性选项id
     */
    private Integer categoryAttrOptionId;
    /**
     * 属性增减价格
     */
    private BigDecimal attrOptionPrice;
    /**
     * 特殊价格
     */
    private BigDecimal specialPrice;

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

    public Integer getCategoryAttrOptionId() {
        return categoryAttrOptionId;
    }

    public void setCategoryAttrOptionId(Integer categoryAttrOptionId) {
        this.categoryAttrOptionId = categoryAttrOptionId;
    }

    public BigDecimal getAttrOptionPrice() {
        return attrOptionPrice;
    }

    public void setAttrOptionPrice(BigDecimal attrOptionPrice) {
        this.attrOptionPrice = attrOptionPrice;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
    }
}
