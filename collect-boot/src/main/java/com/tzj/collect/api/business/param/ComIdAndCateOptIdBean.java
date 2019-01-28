package com.tzj.collect.api.business.param;

import java.util.List;

public class ComIdAndCateOptIdBean {
	
	private String categoryId;//(二级分类id)
	
	private String companyId;//公司id
	
	private String cateOptId;//分类属性选项Id
	
	private String cateOptPrice;//分类属性price

	private String title;//回收物类型
	
	private List<CategoryBean> householdPriceList;//生活垃圾价格列表(id,price)
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCateOptId() {
		return cateOptId;
	}

	public void setCateOptId(String cateOptId) {
		this.cateOptId = cateOptId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<CategoryBean> getHouseholdPriceList() {
		return householdPriceList;
	}

	public void setHouseholdPriceList(List<CategoryBean> householdPriceList) {
		this.householdPriceList = householdPriceList;
	}

	public String getCateOptPrice() {
		return cateOptPrice;
	}

	public void setCateOptPrice(String cateOptPrice) {
		this.cateOptPrice = cateOptPrice;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}
