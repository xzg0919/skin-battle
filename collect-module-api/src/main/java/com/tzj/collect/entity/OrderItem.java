package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;

/**
 *
 *订单分类属性明细
 *
 * @Author 王灿
 **/
@TableName("sb_order_item")
public class OrderItem extends  DataEntity<Long>{
    private Long id;
    /**
     * 订单id
     */
    private  Integer orderId;
    @TableField(exist=false)
    private Order order;
    /**
     * 分类id
     */
    private  Integer categoryId;
    /**
     * 分类名称
     */
    private  String categoryName;
    /**
     * 分类属性id
     */
    private  Integer categoryAttrId;
    /**
     * 分类属性名字
     */
    private String categoryAttrName;
    /**
     * 分类属性选项id
     */
    private  Integer categoryAttrOppId;
    /**
     * 分类属性选项名称
     */
    private  String categoryAttrOpptionName;
    @TableField(exist=false) 
    private CategoryAttr categoryAttr;
    @TableField(exist=false) 
    private String categoryAttrOppIds;
    /**
     * 调整价
     */
    private BigDecimal adjustOrice;
    
    /**
     * 2018年6月27日 09:44:56
     * @author sgmark@aliyun.com
     */
    private double amount;//数量
    
    private float price;//单价
    
    private int parentId;//父类id
    
    private String parentIds;//父类ids
    
    private String unit;//单位
    
    private String parentName;//父类名称
    

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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

	public Integer getCategoryAttrId() {
		return categoryAttrId;
	}

	public void setCategoryAttrId(Integer categoryAttrId) {
		this.categoryAttrId = categoryAttrId;
	}

	public String getCategoryAttrName() {
		return categoryAttrName;
	}

	public void setCategoryAttrName(String categoryAttrName) {
		this.categoryAttrName = categoryAttrName;
	}

	public Integer getCategoryAttrOppId() {
		return categoryAttrOppId;
	}

	public void setCategoryAttrOppId(Integer categoryAttrOppId) {
		this.categoryAttrOppId = categoryAttrOppId;
	}

	public String getCategoryAttrOpptionName() {
		return categoryAttrOpptionName;
	}

	public void setCategoryAttrOpptionName(String categoryAttrOpptionName) {
		this.categoryAttrOpptionName = categoryAttrOpptionName;
	}

	public CategoryAttr getCategoryAttr() {
		return categoryAttr;
	}

	public void setCategoryAttr(CategoryAttr categoryAttr) {
		this.categoryAttr = categoryAttr;
	}

	public String getCategoryAttrOppIds() {
		return categoryAttrOppIds;
	}

	public void setCategoryAttrOppIds(String categoryAttrOppIds) {
		this.categoryAttrOppIds = categoryAttrOppIds;
	}

	public BigDecimal getAdjustOrice() {
		return adjustOrice;
	}

	public void setAdjustOrice(BigDecimal adjustOrice) {
		this.adjustOrice = adjustOrice;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getUnit4Page(){
		StringBuilder builder = null;
		builder =  new StringBuilder("￥ ");
		builder.append(ApiStringUtils.doublegetTwoDecimal(price));
		builder.append("/");
		builder.append(unit);
		return builder.toString();
	}
}
