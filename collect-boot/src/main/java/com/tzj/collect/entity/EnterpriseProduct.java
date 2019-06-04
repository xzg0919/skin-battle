package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 王灿
 * 依旧换新商品表
 */
@TableName("sb_enterprise_product")
public class EnterpriseProduct extends  DataEntity<Long>{

    private static final long serialVersionUID = -6636586372972686143L;
    private Long id;
    /**
     * 企业id
     */
    private Integer enterpriseId;
    /**
     * 产品名
     */
    @TableField(value = "name_")
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
    /**
     * 分类Id
     */
    private Integer categoryId;
    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 创建时间
     */
    @TableField(exist = false)
    private String startTime;

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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
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

    public String getStartTime() {
        return getDate(createDate);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    @Transactional
    public String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
