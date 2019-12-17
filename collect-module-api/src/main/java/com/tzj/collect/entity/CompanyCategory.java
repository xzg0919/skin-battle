package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 *回收企业分类关联表
 *
 * @Author 王灿
 **/
@TableName("sb_company_category")
@Data
public class CompanyCategory extends  DataEntity<Long> {
    private Long id;
    /**
     * 分类id
     */
    private  String categoryId;
    
    @TableField(exist=false)
    private Category category;
    
    /**
     * 企业id
     */
    private  String companyId;
    @TableField(exist=false)
    private Company Company;
    
    private Long parentId;//父类id

	private String parentName;//父类id
    
    private String parentIds;//所有父级编号
    
    private String unit;//单位
    
    private float price; //单价

	private BigDecimal adminCommissions;//平台佣金

	private BigDecimal freeCommissions;//平台订单免费时的佣金

	private BigDecimal companyCommissions;//服务商返佣

	private String isCommissions; //0没有设置 1设置了
    
    
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

	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}
