package com.tzj.collect.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 *	回收企业分类属性选项增减价格关联表
 *
 * @Author zhangqiang
 **/
@TableName("sb_company_category_attr_option")
public class CompanyCategoryAttrOption extends  DataEntity<Long> {
	
	private static final long serialVersionUID = 1L;

	private Long id;

    private  Long companyId;//企业Id

    private  Long categoryAttrOptionId; //分类属性选项Id
    
    private BigDecimal attrOptionPrice; //属性增减价格
    /**
     * 特殊价格
     */
    private BigDecimal specialPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getCategoryAttrOptionId() {
		return categoryAttrOptionId;
	}

	public void setCategoryAttrOptionId(Long categoryAttrOptionId) {
		this.categoryAttrOptionId = categoryAttrOptionId;
	}

	public BigDecimal getAttrOptionPrice() {
		return attrOptionPrice;
	}

	public void setAttrOptionPrice(BigDecimal attrOptionPrice) {
		this.attrOptionPrice = attrOptionPrice;
	}

	public BigDecimal getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(BigDecimal specialPrice) {
		this.specialPrice = specialPrice;
	}
	
}
