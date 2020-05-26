
/**
* @Title: CompanyBean.java
* @date 2018年3月19日 下午3:46:01
* @version V1.0
* @author:[王池]
*/

package com.tzj.collect.core.param.admin;

import com.tzj.collect.core.param.ali.PageBean;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: CompanyBean
 * @date 2018年3月19日 下午3:46:01
 * @author:[王池]
 */

@Data
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
	private Integer cityId;//市id
	private String isOpenOrder;//是否打开自动开 0关闭 1打开

	private String isOpen;
	/**
	 * 类型1 家电  2生活垃圾   4大件
	 */
	private String title;

	private String name;

	private String userName;

	private String password;

	private String tel;

	private String contacts;

	private String longitude; // 经度

	private String latitude; // 纬度

	private  String location;

	private List<String> communityIds;
	
	private String cityName;

	private String areaName;

	private Integer blueTooth;//是否开启蓝牙（0:未开启; 1:开启）

	private Integer directOperation; //是否直营(0:否；1：是)

	private String zhiFuBaoAccount;  //支付宝账号（已授权）

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

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

	public void setInsertCommunityId(Integer insertCommunityId) {
		this.insertCommunityId = insertCommunityId;
	}

	public Integer getDelectCommunityId() {
		return delectCommunityId;
	}

	public void setDelectCommunityId(Integer delectCommunityId) {
		this.delectCommunityId = delectCommunityId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public List<Integer> getCommunityId() {
		return communityId;
	}

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

	public String getIsOpenOrder() {
		return isOpenOrder;
	}

	public void setIsOpenOrder(String isOpenOrder) {
		this.isOpenOrder = isOpenOrder;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
