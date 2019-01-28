package com.tzj.collect.api.admin.param;

import com.tzj.collect.api.ali.param.PageBean;

public class RecyclersBean {

	private String recycleCompany;   //所属服务公司
	
	private String recyclerName;     //回收人员姓名
	
	private Long recyclerId;         //回收人员ID
	
	private String recyclerIdCard; // 回收人员身份证号
	
	private String recyclerSex; //回收人员性别
	
	private String recyclerTel;// 回收人员电话
	
	private String recyclerAdd; // 回收人员地址
	
	private String recyclerICObv; // 身份证正面
	
	private String recyclerICRev;//身份证反面
	
	private String good;	//好评
	
	private String middle;   //中评       
	
	private String bad;       //差评
	
	private Integer  startPage;       //开始页码
	
	private Integer  endPage;       //结束页码
	
	private PageBean page;
	
	public Integer getStartPage() {
		return startPage;
	}

	public Integer getEndPage() {
		return endPage;
	}

	public void setStartPage() {
		this.startPage =(this.page.getPageSize()*this.page.getPageNumber())-this.page.getPageSize();
	}

	public void setEndPage() {
		this.endPage =this.page.getPageSize()*this.page.getPageNumber(); 
	}

	public PageBean getPage() {
		return page;
	}

	public void setPage(PageBean page) {
		this.page = page;
	 	setStartPage();
	 	setEndPage();
	}

	public String getRecycleCompany() {
		return recycleCompany;
	}

	public void setRecycleCompany(String recycleCompany) {
		this.recycleCompany = recycleCompany;
	}


	public String getRecyclerName() {
		return recyclerName;
	}

	public void setRecyclerName(String recyclerName) {
		this.recyclerName = recyclerName;
	}

	public Long getRecyclerId() {
		return recyclerId;
	}

	public void setRecyclerId(Long recyclerId) {
		this.recyclerId = recyclerId;
	}

	public String getRecyclerIdCard() {
		return recyclerIdCard;
	}

	public void setRecyclerIdCard(String recyclerIdCard) {
		this.recyclerIdCard = recyclerIdCard;
	}

	public String getRecyclerSex() {
		return recyclerSex;
	}

	public void setRecyclerSex(String recyclerSex) {
		this.recyclerSex = recyclerSex;
	}

	public String getRecyclerTel() {
		return recyclerTel;
	}

	public void setRecyclerTel(String recyclerTel) {
		this.recyclerTel = recyclerTel;
	}

	public String getRecyclerAdd() {
		return recyclerAdd;
	}

	public void setRecyclerAdd(String recyclerAdd) {
		this.recyclerAdd = recyclerAdd;
	}

	public String getRecyclerICObv() {
		return recyclerICObv;
	}

	public void setRecyclerICObv(String recyclerICObv) {
		this.recyclerICObv = recyclerICObv;
	}

	public String getRecyclerICRev() {
		return recyclerICRev;
	}

	public void setRecyclerICRev(String recyclerICRev) {
		this.recyclerICRev = recyclerICRev;
	}

	public String getGood() {
		return good;
	}

	public void setGood(String good) {
		this.good = good;
	}

	public String getMiddle() {
		return middle;
	}

	public void setMiddle(String middle) {
		this.middle = middle;
	}

	public String getBad() {
		return bad;
	}

	public void setBad(String bad) {
		this.bad = bad;
	}
}
