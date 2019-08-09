package com.tzj.collect.core.param.ali;

import java.math.BigDecimal;

/**
 * 前台分类属性明细
 * @author Michael_Wang
 *
 */
public class OrderItemBean {
	private Long id;
    /**
     * 订单id
     */
    private  int orderId;
    /**
     * 分类id
     */
    private  int categoryId;
    /**
     * 分类名称
     */
    private  String categoryName;
    /**
     * 分类属性id
     */
    private  int categoryAttrId;
    /**
     * 分类属性名字
     */
    private String categoryAttrName;
    /**
     * 分类属性选项id
     */
    private  int categoryAttrOppId;
    /**
     * 分类属性选项名称
     */
    private  String categoryAttrOpptionName;
    
//    @TableField(exist=false) 
    private String categoryAttrOppIds;
    /**
     * 调整价
     */
    private BigDecimal adjustOrice;
    /**
     * 分类父类id
     */
    private String parentId;
    /**
     * 分类父类名称
     */
    private String parentName;
	/**
	 * 价格
	 */
	private String price;

    private double amount;//数量

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCategoryAttrId() {
        return categoryAttrId;
    }

    public void setCategoryAttrId(int categoryAttrId) {
        this.categoryAttrId = categoryAttrId;
    }

    public BigDecimal getAdjustOrice() {
        return adjustOrice;
    }

    public void setAdjustOrice(BigDecimal adjustOrice) {
        this.adjustOrice = adjustOrice;
    }


	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCategoryAttrOppId() {
		return categoryAttrOppId;
	}

	public void setCategoryAttrOppId(int categoryAttrOppId) {
		this.categoryAttrOppId = categoryAttrOppId;
	}

	public String getCategoryAttrOpptionName() {
		return categoryAttrOpptionName;
	}

	public void setCategoryAttrOpptionName(String categoryAttrOpptionName) {
		this.categoryAttrOpptionName = categoryAttrOpptionName;
	}

	public String getCategoryAttrOppIds() {
		return categoryAttrOppIds;
	}

	public void setCategoryAttrOppIds(String categoryAttrOppIds) {
		this.categoryAttrOppIds = categoryAttrOppIds;
	}

	public String getCategoryAttrName() {
		return categoryAttrName;
	}

	public void setCategoryAttrName(String categoryAttrName) {
		this.categoryAttrName = categoryAttrName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
}
