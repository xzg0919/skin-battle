package com.tzj.collect.api.business.param;

import java.util.List;

public class CategoryAttrOptionBean {

	private String id;
	
	private String name;
	
	private List<CompanyCategoryAttrOptionBean> companyCategoryAttrOptionBeanList;

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

	public List<CompanyCategoryAttrOptionBean> getCompanyCategoryAttrOptionBeanList() {
		return companyCategoryAttrOptionBeanList;
	}

	public void setCompanyCategoryAttrOptionBeanList(
			List<CompanyCategoryAttrOptionBean> companyCategoryAttrOptionBeanList) {
		this.companyCategoryAttrOptionBeanList = companyCategoryAttrOptionBeanList;
	}

	
	
	
}
