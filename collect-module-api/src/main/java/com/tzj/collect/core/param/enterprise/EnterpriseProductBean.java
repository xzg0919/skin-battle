package com.tzj.collect.core.param.enterprise;


import com.tzj.collect.core.param.ali.PageBean;

import java.math.BigDecimal;

public class EnterpriseProductBean {

    private String id ;
    /**
     * 产品名
     */
    private String name;
    /**
     * 售价
     */
    private BigDecimal price;
    /**
     * 补贴价格
     */
    private BigDecimal subsidiesPrice;
    /**
     * 产品图片url
     */
    private String picUrl;

    private PageBean pageBean;

    /**
     * 分类Id
     */
    private Integer categoryId;
    /**
     * 分类名称
     */
    private String categoryName;

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubsidiesPrice() {
        return subsidiesPrice;
    }

    public void setSubsidiesPrice(BigDecimal subsidiesPrice) {
        this.subsidiesPrice = subsidiesPrice;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
