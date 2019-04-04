package com.tzj.collect.api.ali.param;

import java.util.List;

public class IdAmountListBean {
	private int categoryParentId;//父类id
	
	private String categoryParentName;//父类名称
	
	private List<OrderItemBean> idAndAmount;//categoryId和数量

	public int getCategoryParentId() {
		return categoryParentId;
	}

	public void setCategoryParentId(int categoryParentId) {
		this.categoryParentId = categoryParentId;
	}

	public String getCategoryParentName() {
		return categoryParentName;
	}

	public void setCategoryParentName(String categoryParentName) {
		this.categoryParentName = categoryParentName;
	}

	public List<OrderItemBean> getIdAndAmount() {
		return idAndAmount;
	}

	public void setIdAndAmount(List<OrderItemBean> idAndAmount) {
		this.idAndAmount = idAndAmount;
	}
}
