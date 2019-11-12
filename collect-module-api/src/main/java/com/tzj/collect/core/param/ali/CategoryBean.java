package com.tzj.collect.core.param.ali;

import lombok.Data;

import java.util.List;

/**
 * 分类相关bean
 * @author Michael_Wang
 *
 */
@Data
public class CategoryBean {

	//id
	private Integer id;
	//上级分类
	private Integer parentId;
	//分类名称
	private String name;
	//分类code
	private String code;
	
	private Integer communityId;//小区id

	private Integer streeId;//街道id
	
	private PageBean pageBean;
	
    private Integer areaId;

	private String orderId;
    /**
     * 企业id 
     */
    private Integer companyId;
	
    //最上层
    private String title;
    
	//层级
	private Integer level;
	//是否免费
	private String isCash;
	/**
	 * 区域Id
	 */
	private String cityId;

	/**
	 * 类型
	 */
	private String type;
	/**
	 * 是否是五公斤废纺衣物回收
	 */
	private String isFiveKg;
	/**
	 * 经纬度
	 */
	private String location;
	
	//分类属性
	private List<CategoryAttrBean> categoryAttr;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getIsFiveKg() {
		return isFiveKg;
	}

	public void setIsFiveKg(String isFiveKg) {
		this.isFiveKg = isFiveKg;
	}

	public Integer getStreeId() {
		return streeId;
	}

	public void setStreeId(Integer streeId) {
		this.streeId = streeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public List<CategoryAttrBean> getCategoryAttr() {
		return categoryAttr;
	}

	public void setCategoryAttr(List<CategoryAttrBean> categoryAttr) {
		this.categoryAttr = categoryAttr;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getIsCash() {
		return isCash;
	}

	public void setIsCash(String isCash) {
		this.isCash = isCash;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
