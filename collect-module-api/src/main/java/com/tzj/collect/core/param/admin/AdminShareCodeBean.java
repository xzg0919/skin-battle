package com.tzj.collect.core.param.admin;

import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.entity.LineQrCode;
import lombok.Data;

import java.util.List;

@Data
public class AdminShareCodeBean {

	private LineQrCode.QrType qrType;

	private String qrName;

	private String qrCodeInfo;

	private List<AdminAddressBean> adminCityList;

	private PageBean pageBean;

	private String qrCode;

	private Long streetId;//街道

	private Long areaId;//区

	private Long  cityId;//市

	private Long  provinceId;//省

	private String streetName;

	private String areaName;

	private String cityName;

	private String provinceName;

	private String startTime;

	private String endTime;
}
