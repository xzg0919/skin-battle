package com.tzj.collect.core.param.app;

import com.tzj.collect.core.param.ali.PageBean;
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
}
