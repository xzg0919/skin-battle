package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.tzj.collect.core.result.business.BusinessCategoryResult;
import lombok.Data;

import java.util.List;
import java.util.Map;

@TableName("sb_category_attr")
@Data
public class CategoryAttr extends DataEntity<Long> {
	private Long id;
	@TableField(value="name_")
	private String name;   //属性名称
	@TableField(value="sort_")
	private Integer sort;   //排序
	private Integer categoryId;   //分类id
	private String createBy;
	@TableField(exist=false)
    private Category category;
	@TableField(exist=false)
	private CategoryAttrOption categoryAttrOption;
	@TableField(exist=false)
	private List<CategoryAttrOption>  CategoryAttrOptionList;
	@TableField(exist=false)
	private List<BusinessCategoryResult> resultList;
	@TableField(exist=false)
	private List<Map<String,Object>> objectMap;
	/**
	 * 企业Id
	 */
	@TableField(exist=false)
	private Integer companyId;
	/**
	 * 小区Id
	 */
	@TableField(exist=false)
	private Integer communityId;
	

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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}


	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public CategoryAttrOption getCategoryAttrOption() {
		return categoryAttrOption;
	}

	public void setCategoryAttrOption(CategoryAttrOption categoryAttrOption) {
		this.categoryAttrOption = categoryAttrOption;
	}

	public List<CategoryAttrOption> getCategoryAttrOptionList() {
		return CategoryAttrOptionList;
	}

	public void setCategoryAttrOptionList(List<CategoryAttrOption> categoryAttrOptionList) {
		CategoryAttrOptionList = categoryAttrOptionList;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

	public List<BusinessCategoryResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<BusinessCategoryResult> resultList) {
		this.resultList = resultList;
	}
	
}
