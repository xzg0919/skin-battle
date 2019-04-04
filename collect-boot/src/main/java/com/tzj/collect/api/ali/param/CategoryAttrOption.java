package com.tzj.collect.api.ali.param;

import java.math.BigDecimal;

/**
 * 分类属性选项
 * @author Michael_Wang
 *
 */
public class CategoryAttrOption {
	
	//id
	private Long id;
	
	//name
	private String name;
	
	private Integer sort;    //排序
	
	//分类id
	private Long categoryId;
	
	private CategoryAttrBean categoryAttr;
	
	private String price;  //调整价
	
	private String isSpecial;
	
	private String specialPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public CategoryAttrBean getCategoryAttr() {
		return categoryAttr;
	}

	public void setCategoryAttr(CategoryAttrBean categoryAttr) {
		this.categoryAttr = categoryAttr;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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
