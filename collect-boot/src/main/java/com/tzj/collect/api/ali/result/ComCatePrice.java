package com.tzj.collect.api.ali.result;

public class ComCatePrice {
	private Integer id;
	
	private String icon;
	
	private String name;
	
	private float price;
	
	private String parentName;

	private String parentId;

	private String unit;

	private String isOldExchangeNew;

	public String getIsOldExchangeNew() {
		return isOldExchangeNew;
	}

	public void setIsOldExchangeNew(String isOldExchangeNew) {
		this.isOldExchangeNew = isOldExchangeNew;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPriceAndUnit() {
		return "￥" +  price + "/" + unit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
