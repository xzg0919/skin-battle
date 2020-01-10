package com.tzj.collect.core.param.admin;

import lombok.Data;

@Data
public class AdminAddressBean {
	
	private Long streetId; //街道/镇

	private Long areaId;//区

	private Long  cityId;//市

	private Long  provinceId;//省

	private String streetName;//街道名称

	private String areaName;

	private String cityName;

	private String provinceName;

	private String isSelect = "0";

}
