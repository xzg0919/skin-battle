package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("sb_community")
public class Community extends DataEntity<Long> {

	private Long id;
	private Integer areaId; // 父级编号
	private String parentIds; // 所有父级编号\
	@TableField(value = "name_")
	private String name; // 名称
	private String address; // 地址
	private String zipcode; // 邮编
	private Double longitude; // 经度
	private Double latitude; // 纬度
	@TableField(exist = false)
	private String selected;// 0未选中的小区,1已选中的小区
	/**
	 * 是否免费 0不免费 1免费
	 */
	private String isFree;
	/**
	 * 定点回收时间 每周二、四 06:00-18:00,每周三、五 09:00-10:00
	 */
	private String fixedPointTime;
	/**
	 * 定点回收地址 盛达家园北门垃圾场附近
	 */
	private String fixedPointAddress;

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public Community(boolean isExist) {
		super();
		this.isExist = isExist;
	}
	
	private String initials; // 首字母
	@TableField(exist = false)
	private Area area;
	@TableField(exist = false)
	private Community community;
	@TableField(exist = false)
	private boolean isExist;
	@TableField(exist = false)
	private String juli;// 0未选中的小区,1已选中的小区

	public boolean getIsExist() {
		return isExist;
	}

	public void setIsExist(boolean isExist) {
		this.isExist = isExist;
	}
	

	public String getJuli() {
		return juli;
	}

	public void setJuli(String juli) {
		this.juli = juli;
	}

	public Community() {
		super();
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public String getIsFree() {
		return isFree;
	}

	public void setIsFree(String isFree) {
		this.isFree = isFree;
	}

	public String getFixedPointTime() {
		return fixedPointTime;
	}

	public void setFixedPointTime(String fixedPointTime) {
		this.fixedPointTime = fixedPointTime;
	}

	public String getFixedPointAddress() {
		return fixedPointAddress;
	}

	public void setFixedPointAddress(String fixedPointAddress) {
		this.fixedPointAddress = fixedPointAddress;
	}

	
	
}
