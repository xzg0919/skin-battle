package com.tzj.collect.core.param.admin;

import lombok.Data;

@Data
public class AdminCommunityBean {
	
	private String countyId; //行政区/县
	private String countyName;// 行政区名称
	private String streetId; //街道/镇
	private String streetName;//街道名称
	private String communityId;//小区id
	private String communityName;//小区名称
	private String selected;// 1公司已添加服务范围的小区

	private Long areaId;//区

	private Long  cityId;//市

	private Long  provinceId;//省

	private String areaName;

	private String cityName;

	private String provinceName;

}
