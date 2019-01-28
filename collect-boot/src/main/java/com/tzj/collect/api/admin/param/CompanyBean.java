
/**
* @Title: CompanyBean.java
* @date 2018年3月19日 下午3:46:01
* @version V1.0
* @author:[王池]
*/

package com.tzj.collect.api.admin.param;

import java.util.List;

import com.tzj.collect.api.ali.param.PageBean;

/**
 * @ClassName: CompanyBean
 * @date 2018年3月19日 下午3:46:01
 * @author:[王池]
 */

public class CompanyBean {
	private Long id;//公司id

	private String CompanyName;// 回收公司名称
	/**
	 * 分页
	 */
	private PageBean pageBean;
	private String telphone; // 联系电话

	private List<Integer> category_id;//新增 回收公司时所选中的分类id
	private List<Integer>communityId;//新增回收公司时所选中的小区的id
	private Integer areaId;// 服务范围id（可能是区县id可能是街道id）
    private Integer streetId;//街道id
	private Integer countyId;//区县id
	
	
	
	public Integer getStreetId() {
		return streetId;
	}

	
	public void setStreetId(Integer streetId) {
		this.streetId = streetId;
	}



	public Integer getCountyId() {
		return countyId;
	}


	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}



	private Integer insertCommunityId;//编辑回收范围时添加的小区的id
	private Integer delectCommunityId;//编辑回收范围时删除的小区的id
	
	/**
	* @return insertCommunityId
	*/
	
	public Integer getInsertCommunityId() {
		return insertCommunityId;
	}


	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setInsertCommunityId(Integer insertCommunityId) {
		this.insertCommunityId = insertCommunityId;
	}


	
	/**
	* @return delectCommunityId
	*/
	
	public Integer getDelectCommunityId() {
		return delectCommunityId;
	}


	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setDelectCommunityId(Integer delectCommunityId) {
		this.delectCommunityId = delectCommunityId;
	}


	public Integer getAreaId() {
		return areaId;
	}

	
	/**
	* @return communityId
	*/
	
	public List<Integer> getCommunityId() {
		return communityId;
	}

	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setCommunityId(List<Integer> communityId) {
		this.communityId = communityId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getTelphone() {
		return telphone;
	}

	public List<Integer> getCategory_id() {
		return category_id;
	}

	public void setCategory_id(List<Integer> category_id) {
		this.category_id = category_id;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	private String address; // 地址

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

	public String getCompanyName() {
		return CompanyName;
	}

	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
}
