package com.tzj.collect.core.param.app;

import com.tzj.collect.core.param.ali.PageBean;

import java.util.List;

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

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getAliUserId() {
		return aliUserId;
	}

	public void setAliUserId(String aliUserId) {
		this.aliUserId = aliUserId;
	}

	public String getAliAccountNumber() {
		return aliAccountNumber;
	}

	public void setAliAccountNumber(String aliAccountNumber) {
		this.aliAccountNumber = aliAccountNumber;
	}

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getIdCardRev() {
		return idCardRev;
	}

	public void setIdCardRev(String idCardRev) {
		this.idCardRev = idCardRev;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdCardObv() {
		return idCardObv;
	}

	public void setIdCardObv(String idCardObv) {
		this.idCardObv = idCardObv;
	}

	public List<String> getComIdsList() {
		return comIdsList;
	}

	public void setComIdsList(List<String> comIdsList) {
		this.comIdsList = comIdsList;
	}

	public String getIsReal() {
		return isReal;
	}

	public void setIsReal(String isReal) {
		this.isReal = isReal;
	}
}
