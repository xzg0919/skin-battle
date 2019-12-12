package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

@TableName("sb_category_attr_option")
@Data
public class CategoryAttrOption extends DataEntity<Long> {


	private Long id;
	@TableField(value="name_")
	private String name;   //属性名称
	@TableField(value="sort_")
	private Integer sort;    //排序
	private Long categoryAttrId;  //分类属性id
	private BigDecimal price;  //调整价
	/**
	 * 是否是特殊的分类属性0不是，1是
	 */
	private String isSpecial;
	@TableField(exist=false)
    private CategoryAttr categoryAttr;

    private BigDecimal specialPrice;
	@TableField(exist=false)
	private String isRecovery;
	@Override
	public Long getId() {	
		return id;
	}

	@Override
	public void setId(Long id) {
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

	public Long getCategoryAttrId() {
		return categoryAttrId;
	}

	public void setCategoryAttrId(Long categoryAttrId) {
		this.categoryAttrId = categoryAttrId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public CategoryAttr getCategoryAttr() {
		return categoryAttr;
	}

	public void setCategoryAttr(CategoryAttr categoryAttr) {
		this.categoryAttr = categoryAttr;
	}

	public String getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	public BigDecimal getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(BigDecimal specialPrice) {
		this.specialPrice = specialPrice;
	}

	
	
}
