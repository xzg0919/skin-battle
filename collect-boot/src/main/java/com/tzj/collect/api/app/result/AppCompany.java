package com.tzj.collect.api.app.result;

import java.util.List;

public class AppCompany {
	
	private String comIds; 
	
	private String comName;//返回公司名称
	
	private String id;//公司Id
	
	private String tel;
	
	private String categId;
	
	private String cateName;
	
	private List<String> categList;

	private String isBigRecycle;
	
	private String status;//认证状态

	public String getIsBigRecycle() {
		return isBigRecycle;
	}

	public void setIsBigRecycle(String isBigRecycle) {
		this.isBigRecycle = isBigRecycle;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComIds() {
		return comIds;
	}

	public void setComIds(String comIds) {
		this.comIds = comIds;
	}

	public List<String> getCategList() {
		return categList;
	}

	public void setCategList(List<String> categList) {
		this.categList = categList;
	}

	public String getCategId() {
		return categId;
	}

	public void setCategId(String categId) {
		this.categId = categId;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
 }
