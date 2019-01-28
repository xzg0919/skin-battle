package com.tzj.collect.api.business.param;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;

import com.tzj.collect.api.admin.param.RecyclersBean;
import com.tzj.collect.api.ali.param.PageBean;

public class CompanyAccountBean {
	
	private String id;
	
	private String username;
	
	private String password;
	
	private PageBean pageBean;
	
	private RecyclersBean recBean;
	
	private ServletServerHttpRequest request;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RecyclersBean getRecBean() {
		return recBean;
	}

	public void setRecBean(RecyclersBean recBean) {
		this.recBean = recBean;
	}

	public ServletServerHttpRequest getRequest() {
		return request;
	}

	public void setRequest(ServletServerHttpRequest request) {
		this.request = request;
	}

	
	
}
