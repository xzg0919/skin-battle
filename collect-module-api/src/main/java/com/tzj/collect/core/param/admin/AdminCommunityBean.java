package com.tzj.collect.core.param.admin;

public class AdminCommunityBean {
	
	private String countyId; //行政区/县
	private String countyName;// 行政区名称
	private String streetId; //街道/镇
	private String streetName;//街道名称
	private String communityId;//小区id
	private String communityName;//小区名称
	private String selected;// 1公司已添加服务范围的小区
	
		
	public String getSelected() {
		return selected;
	}

	
	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getStreetId() {
		return streetId;
	}

	public void setStreetId(String streetId) {
		this.streetId = streetId;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	
}
