package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
*
*会员地址表
*
* @Author 王灿
**/
@TableName("sb_member_address")
public class MemberAddress extends  DataEntity<Long>{
     private Long id;
	 /**
	  * 会员id
	  */
	 private String memberId;
	 /**
	  * 区域Id
	  */
	 private Integer areaId;
	 /**
	  * 街道Id
	  */
	 private Integer streetId;
	 /**
	  * 小区Id
	  */
	 private Integer communityId;
	 /**
	  * 地址
	  */
	 @TableField(value="address_")
	 private String address;
	 /**
	  * 用户输入小区地址
	  */
	 private String commByUserInput;
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
	 private Integer isSelected;
	 /**
	  * 姓名
	  */
	 @TableField(value="name_")
	 private String name;
	 /**
	  * 市级Id
	  */
	 private Integer cityId;
	 
	 /**
	  * 是否有定点回收
	  */
	 @TableField(exist = false)
	 private String isFixedPoint;
	/**
	 * 是否回收电器
	 */
	@TableField(exist = false)
	private String isDigital;
	/**
	 * 是否回收生活垃圾
	 */
	@TableField(exist = false)
	private String isHousehold;
	/**
	 * 是否回收5公斤废纺衣物
	 */
	@TableField(exist = false)
	private String isFiveKg;

	public String getIsFiveKg() {
		return isFiveKg;
	}

	public void setIsFiveKg(String isFiveKg) {
		this.isFiveKg = isFiveKg;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getStreetId() {
		return streetId;
	}

	public void setStreetId(Integer streetId) {
		this.streetId = streetId;
	}

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
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

	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getIsFixedPoint() {
		return isFixedPoint;
	}

	public void setIsFixedPoint(String isFixedPoint) {
		this.isFixedPoint = isFixedPoint;
	}

	public String getIsDigital() {
		return isDigital;
	}

	public void setIsDigital(String isDigital) {
		this.isDigital = isDigital;
	}

	public String getIsHousehold() {
		return isHousehold;
	}

	public void setIsHousehold(String isHousehold) {
		this.isHousehold = isHousehold;
	}
}
