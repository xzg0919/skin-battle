package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
/**
 * 企业表
* @ClassName: Company
* @date 2018年3月20日 上午9:23:05
* @author:[王池]
 */
@TableName("sb_company")
public class Company extends DataEntity<Long> {
	private Long id;
	@TableField(value="name_")
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
	private String dingDingUrl;   //钉钉通知的连接
	/**
	 * 回收企业类型，回收企业，智能设备回收企业,回收企业并且是智能设备回收企业
	 */
	private String companyType;
	
	@TableField(exist=false)
	private Integer recyclerCount;//企业绑定的回收人员数目
	@TableField(exist=false)
	private Integer companyServiceCount;//该企业回收服务类型个数
	@TableField(exist=false)
	private Integer companyCategoryCount;//该回收服务类型个数
	
	

	
	/**
	* @return recyclerCount
	*/
	
	public Integer getRecyclerCount() {
		return recyclerCount;
	}

	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setRecyclerCount(Integer recyclerCount) {
		this.recyclerCount = recyclerCount;
	}

	
	/**
	* @return companyServiceCount
	*/
	
	public Integer getCompanyServiceCount() {
		return companyServiceCount;
	}

	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setCompanyServiceCount(Integer companyServiceCount) {
		this.companyServiceCount = companyServiceCount;
	}

	
	/**
	* @return companyCategoryCount
	*/
	
	public Integer getCompanyCategoryCount() {
		return companyCategoryCount;
	}

	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setCompanyCategoryCount(Integer companyCategoryCount) {
		this.companyCategoryCount = companyCategoryCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}


	public String getCompanyType() {
		return companyType;
	}


	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}


	public String getDingDingUrl() {
		return dingDingUrl;
	}


	public void setDingDingUrl(String dingDingUrl) {
		this.dingDingUrl = dingDingUrl;
	}
	
}
