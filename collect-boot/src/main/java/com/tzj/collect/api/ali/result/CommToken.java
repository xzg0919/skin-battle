package com.tzj.collect.api.ali.result;

public class CommToken {
	
	private String commName;//小区名称
	
	private String tencentToken;//回收人员端的腾讯token
	
	public String getCommName() {
		return commName;
	}
	public void setCommName(String commName) {
		this.commName = commName;
	}
	public String getTencentToken() {
		return tencentToken;
	}
	public void setTencentToken(String tencentToken) {
		this.tencentToken = tencentToken;
	}
	
	
}
