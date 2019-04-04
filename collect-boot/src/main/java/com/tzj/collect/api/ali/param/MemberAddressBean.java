package com.tzj.collect.api.ali.param;

/**
 * 会员地址
 * @author Administrator
 *
 */
public class MemberAddressBean {
	
	 private String id;
	 /**
	  * 会员id
	  */
	 private String memberId;
	 /**
	  * 区域Id
	  */
	 private String areaId;
	/**
	 * 区域名字
	 */
	private String areaName;
	 /**
	  * 街道Id
	  */
	 private String streetId;
	/**
	 * 街道名字
	 */
	private String streetName;
	 /**
	  * 小区Id
	  */
	 private String communityId;
	/**
	 * 小区名字
	 */
	private String communityName;
	 
	 /**
	  * 用户输入小区地址
	  */
	 private String commByUserInput;
	 /**
	  * 地址
	  */
	 private String address;
	 /**
	  * 门牌编号
	  */
	 private String houseNumber;
	 /**
	  * 电话
	  */
	 private String tel;
	 /**
	  * 是否是默认 1代表是默认
	  */
	 private String isSelected;
	 /**
	  * 姓名
	  */
	 private String name;
	 /**
	  * 市级Id
	  */
	 private Integer cityId;


	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
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

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getStreetId() {
		return streetId;
	}
	public void setStreetId(String streetId) {
		this.streetId = streetId;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
	public String getCommByUserInput() {
		return commByUserInput;
	}
	public void setCommByUserInput(String commByUserInput) {
		this.commByUserInput = commByUserInput;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	
}
