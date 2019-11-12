package com.tzj.collect.api.business.result;

public class BusinessCategoryResult {

	private String attOptionId;//属性选项Id
	
	private String attOptionName;//属性选项name
	
	private String comOptPriceId;//公司定义选项价格Id
	
	private String comOptPrice;//公司定义增减价格
	
	private String comOptAddPrice;//公司定义增加价格
	
	private String comOptDecprice;//公司定义减少价格
	
	private String isSpecial;//是否是特殊字段
	
	private String specialPrice;//是否是特殊价格

	public String getAttOptionId() {
		return attOptionId;
	}

	public void setAttOptionId(String attOptionId) {
		this.attOptionId = attOptionId;
	}

	public String getAttOptionName() {
		return attOptionName;
	}

	public void setAttOptionName(String attOptionName) {
		this.attOptionName = attOptionName;
	}

	public String getComOptPriceId() {
		return comOptPriceId;
	}

	public void setComOptPriceId(String comOptPriceId) {
		this.comOptPriceId = comOptPriceId;
	}

	public String getComOptAddPrice() {
		return comOptAddPrice;
	}

	public void setComOptAddPrice(String comOptAddPrice) {
		this.comOptAddPrice = comOptAddPrice;
	}

	public String getComOptDecprice() {
		return comOptDecprice;
	}

	public void setComOptDecprice(String comOptDecprice) {
		this.comOptDecprice = comOptDecprice;
	}

	public String getComOptPrice() {
		return comOptPrice;
	}

	public void setComOptPrice(String comOptPrice) {
		this.comOptPrice = comOptPrice;
	}

	public String getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	public String getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}
	
}
