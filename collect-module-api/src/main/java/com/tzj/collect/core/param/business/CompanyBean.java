package com.tzj.collect.core.param.business;

import lombok.Data;

@Data
public class CompanyBean {
	private String id; //企业ID
	private String name;   //企业名称
	private String companyCode;  //企业编号
	private String contacts;   //联系人
	private String tel;   //联系电话
	private String address;   //地址
	private String introduction;   //简介
	private String icon;   //图标
	private String website;   //网址
	private String email;   //电子邮件
	private String zipcode;    //邮编
	private String orgCode;   //组织机构编码
	private String startTime; //开始时间

	private String code;

	private String type;//PRICE 查订单金额  NUM 订单笔数
	private String date;//D 查询今天  W 代表查近一周 M代表查近一个月
	
}
