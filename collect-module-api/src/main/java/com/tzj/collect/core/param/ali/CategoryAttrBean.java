package com.tzj.collect.core.param.ali;

import java.util.List;

/**
 * 分类属性
 * @author Michael_Wang
 *
 */
public class CategoryAttrBean {
	//id
	private Long id;
	//分类id
	private Long categoryId;
	//分类选项Id
	private String categoryAttrOptionId;
	//分类选项价格
	private String categoryAttrOptionPrices;
	//小区Id
	private String communityId;
	//行政区Id
	private String cityId;
	
	private Integer weight;
	
	private CategoryBean category;

	private String token;

	private String categoryAttrOptionids;

	private String type;
	
	//选项
	private List<CategoryAttrOption> optionList;


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public CategoryBean getCategory() {
		return category;
	}
	public void setCategory(CategoryBean category) {
		this.category = category;
	}
	public List<CategoryAttrOption> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<CategoryAttrOption> optionList) {
		this.optionList = optionList;
	}
	public String getCategoryAttrOptionId() {
		return categoryAttrOptionId;
	}
	public void setCategoryAttrOptionId(String categoryAttrOptionId) {
		this.categoryAttrOptionId = categoryAttrOptionId;
	}
	public String getCategoryAttrOptionPrices() {
		return categoryAttrOptionPrices;
	}
	public void setCategoryAttrOptionPrices(String categoryAttrOptionPrices) {
		this.categoryAttrOptionPrices = categoryAttrOptionPrices;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCategoryAttrOptionids() {
		return categoryAttrOptionids;
	}

	public void setCategoryAttrOptionids(String categoryAttrOptionids) {
		this.categoryAttrOptionids = categoryAttrOptionids;
	}
}
