package com.tzj.collect.core.param.business;

import com.tzj.collect.core.param.ali.PageBean;

import java.util.List;

public class BusinessRecyclerBean {
	private String telephone;//回收人员电话
	private String idCard;//回收人员ID
	private Long companyId;//公司id
	private PageBean page;//分页
	private List<BusinessRecyclerBean>dataList;
    private String recyclerName;     //回收人员姓名	
	private Long id;         //回收人员id
	private String delFlag;//0表示启用，1表示禁用
	private String applyStatus;//回收人员的申请状态，1同意，2拒绝

	private String headPicUrl;
	private String isBigRecycle;

	public String getIsBigRecycle() {
		return isBigRecycle;
	}

	public void setIsBigRecycle(String isBigRecycle) {
		this.isBigRecycle = isBigRecycle;
	}

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	public String getRecyclerName() {
		return recyclerName;
	}
	public void setRecyclerName(String recyclerName) {
		this.recyclerName = recyclerName;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public PageBean getPage() {
		return page;
	}
	public void setPage(PageBean page) {
		this.page = page;
	}
	public List<BusinessRecyclerBean> getDataList() {
		return dataList;
	}
	public void setDataList(List<BusinessRecyclerBean> dataList) {
		this.dataList = dataList;
	}

	
	
	
	
}
