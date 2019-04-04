package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 企业,分类,区域关联表
 * @author wangcan
 *
 */
@TableName("sb_company_share")
public class CompanyShare extends DataEntity<Long>{
	private Long id;
	/**
	 * 行政区Id（例 黄浦区：241 ）
	 */
	private Integer areaId;
	/**
	 * 企业Id
	 */
	private Integer companyId;
	/**
	 * 分类Id（最大的分类）
	 */
	private Integer categoryId;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
