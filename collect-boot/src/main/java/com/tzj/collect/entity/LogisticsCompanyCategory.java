package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;

/**
 * 第三方物流公司类型
 */
@TableName("sb_logistics_company_category")
public class LogisticsCompanyCategory extends  DataEntity<Long>{

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
     * 分类Id
     */
    private Integer categoryId;
    /**
     * 分类父级Id
     */
    private Integer parentId;
    /**
     * 价格
     */
    private BigDecimal price;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
