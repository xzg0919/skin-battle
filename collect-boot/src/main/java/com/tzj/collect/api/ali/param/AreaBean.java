package com.tzj.collect.api.ali.param;

import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.api.business.param.CommunityBean;
import com.tzj.collect.entity.Community;

import java.util.List;

/**
 * 区域对象
 * @author Michael_Wang
 *
 */
public class AreaBean {
	//id
	private Integer id;
	
	//level
	private Integer level;
	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 纬度
	 */
	private String latitude;
	/**
	 * 行政市Id 例上海市Id
	 */
	private String cityId;
	/**
	 * 行政区名字
	 */
	private String areaName;
	/**
	 * 街道名字
	 */
	private String streetName;
	/**
	 * 街道Id
	 */
	private String streeId;
	/**
	 * 区域Id
	 */
	private String areaId;
	/**
	 * 0保存，1删除
	 */
	private String saveOrDelete;

	private List<CommunityBean> communityIdList;

	public String getSaveOrDelete() {
		return saveOrDelete;
	}

	public void setSaveOrDelete(String saveOrDelete) {
		this.saveOrDelete = saveOrDelete;
	}

	public List<CommunityBean> getCommunityIdList() {
		return communityIdList;
	}

	public void setCommunityIdList(List<CommunityBean> communityIdList) {
		this.communityIdList = communityIdList;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getStreeId() {
		return streeId;
	}

	public void setStreeId(String streeId) {
		this.streeId = streeId;
	}

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
	
}
