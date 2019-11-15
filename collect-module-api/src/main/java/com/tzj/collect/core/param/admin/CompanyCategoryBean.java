package com.tzj.collect.core.param.admin;

import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Company;

public class CompanyCategoryBean {
	private Long id;
    /**
     * 分类id
     */
    private  String categoryId;
    
    private Category category;
    
    /**
     * 企业id
     */
    private  String companyId;
    
    private Company Company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Company getCompany() {
		return Company;
	}

	public void setCompany(Company company) {
		Company = company;
	}
    
    
}
