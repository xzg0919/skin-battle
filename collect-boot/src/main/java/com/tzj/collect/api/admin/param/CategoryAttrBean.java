package com.tzj.collect.api.admin.param;


public class CategoryAttrBean {
	private String  id;
	
	private String name;   //属性名称
	
	private Integer sort;   //排序
	
	private Integer categoryId;   //分类id
    
    private String categoryAttrOptionId;//分类选项Id
    
    private String categoryAttrOptionIds;//多个分类选项Id
    
    private String categoryAttrOptionNames;//多个分类选项名称
    
    private String categoryAttrOptionPrices;//多个分类选项调整价格

	

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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryAttrOptionId() {
		return categoryAttrOptionId;
	}

	public void setCategoryAttrOptionId(String categoryAttrOptionId) {
		this.categoryAttrOptionId = categoryAttrOptionId;
	}

	public String getCategoryAttrOptionIds() {
		return categoryAttrOptionIds;
	}

	public void setCategoryAttrOptionIds(String categoryAttrOptionIds) {
		this.categoryAttrOptionIds = categoryAttrOptionIds;
	}

	public String getCategoryAttrOptionNames() {
		return categoryAttrOptionNames;
	}

	public void setCategoryAttrOptionNames(String categoryAttrOptionNames) {
		this.categoryAttrOptionNames = categoryAttrOptionNames;
	}

	public String getCategoryAttrOptionPrices() {
		return categoryAttrOptionPrices;
	}

	public void setCategoryAttrOptionPrices(String categoryAttrOptionPrices) {
		this.categoryAttrOptionPrices = categoryAttrOptionPrices;
	}

    
}
