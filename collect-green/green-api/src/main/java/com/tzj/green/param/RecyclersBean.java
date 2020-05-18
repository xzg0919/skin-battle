package com.tzj.green.param;

import lombok.Data;

import java.util.List;

@Data
public class RecyclersBean {

		//id
	private Long id;  
	private String name; //姓名
	private String sex;//性别
	private String address; //地址
	private String password;
	private String idCard;//身份证
	private String idCardObv;//身份证反面
	private String idCardRev;//身份证反面
	// 初始状态
	private String status="1";

	private String parentId;

	private List<String> comIdsList;
	//pageBean
	private PageBean pageBean;
	//是否芝麻实名 0未实名 1实名
	private String isReal;
	/**
	 * 头像Url
	 */
	private String headPicUrl;
	/**
	 * 支付宝账号
	 */
	private String aliAccountNumber;

	private String aliUserId;

	private String authCode;

	private String isBigRecycle;

	private String certifyId;

	private String mobile;

	private Long companyId;

	private String  provinceId;

	private String  provinceName;

	private String  cityId;

	private String  cityName;

	private String  areaId;

	private String  areaName;

	private String  streetId;

	private String  streetName;

	private String  communityId;

	private String  communityName;

	private String  houseName;

	/**
	 * 详细地址
	 */
	private String detailAddress;

	private String realNo;//实体卡号

	private String  captcha;//验证码

	private String  lat;//31.29058

	private String  lng;//121.554966

	private Long recId;
}
