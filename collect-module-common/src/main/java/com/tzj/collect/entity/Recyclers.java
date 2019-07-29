
/**
* @Title: SbRecyclers.java
* @Package com.tzj.collect.entity
* @Description: 【】
* @date 2018年3月5日 下午1:18:42
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @ClassName: SbRecyclers
 * @Description: 【】
 * @date 2018年3月5日 下午1:18:42
 * @Company: 上海挺之军科技有限公司
 * @Department：研发部
 * @author:[王池][wjc2013481273@163.com]
 */
@TableName("sb_recyclers")
public class Recyclers extends DataEntity<Long> {
	private Long id;// 编号
	@TableField(value = "name_")
	private String name;// 姓名
	private String idCard;// 身份证
	private String tel;// 电话
	@TableField(value = "status_")
	private String status="0";// 开工状态 0:开工,1:收工
	private String icon;// 照片
	@TableField(value = "auth_status")
	private String authStatus="0";// 0未认证状态，1已认证
	@TableField(value = "id_card_obv")
	private String idCardObv;// 身份证正面
	@TableField(value = "id_card_rev")
	private String idCardRev;// 身份证反面
	private String sex;// 性别
	private String address;// 地址
	@TableField(exist=false)
	private Integer initCount;//获取回收人员待接单数量
	@TableField(exist=false)
	private Integer alreadyCount;//获取回收人员进行中订单数量
	@TableField(exist=false)
	private Integer cancelCount;//获取回收人员进取消订单数量
	private String tencentToken;//token
	@TableField(exist=false)
	private Integer    companyCount;   //该回收人员和回收公司的绑定数目
 	@TableField(exist=false)
    private String companyName;

 	private String password;


	//芝麻认证码
	private String bizNo;
	//是否芝麻实名 0未实名 1实名
	private String isReal="0";
	/**
	 * 头像Url
	 */
	private String headPicUrl;
	/**
	 * 支付宝账号
	 */
	private String aliAccountNumber;

	private String aliUserId;

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

	public Integer getCompanyCount() {
		return companyCount;
	}

	public void setCompanyCount(Integer companyCount) {
		this.companyCount = companyCount;
	}
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getIdCardObv() {
		return idCardObv;
	}

	public void setIdCardObv(String idCardObv) {
		this.idCardObv = idCardObv;
	}

	public String getIdCardRev() {
		return idCardRev;
	}

	public void setIdCardRev(String idCardRev) {
		this.idCardRev = idCardRev;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param paramtheparamthe{bare_field_name}
	 *            to set
	 */

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {

		return id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getId_card_obv() {
		return idCardObv;
	}

	public void setId_card_obv(String id_card_obv) {
		this.idCardObv = id_card_obv;
	}

	public Integer getInitCount() {
		return initCount;
	}

	public void setInitCount(Integer initCount) {
		this.initCount = initCount;
	}

	public Integer getAlreadyCount() {
		return alreadyCount;
	}

	public void setAlreadyCount(Integer alreadyCount) {
		this.alreadyCount = alreadyCount;
	}

	public Integer getCancelCount() {
		return cancelCount;
	}

	public void setCancelCount(Integer cancelCount) {
		this.cancelCount = cancelCount;
	}
	
	public String getTencentToken() {
		return tencentToken;
	}

	public void setTencentToken(String tencentToken) {
		this.tencentToken = tencentToken;
	}

	public String getIsReal() {
		return isReal;
	}

	public void setIsReal(String isReal) {
		this.isReal = isReal;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"name\":\"" + name + "\", \"idCard\":\"" + idCard + "\", \"tel\":\"" + tel + "\", \"status\":\"" + status
				+ "\", \"icon\":\"" + icon + "\", \"authStatus\":\"" + authStatus + "\", \"idCardObv\":\"" + idCardObv + "\", \"idCardRev\":\""
				+ idCardRev + "\", \"sex\":\"" + sex + "\", \"address\":\"" + address + "\", \"updateDate\":\"" + updateDate + "\", \"delFlag\":\""
				+ delFlag + "\", \"createDate\":\"" + createDate + "\"}";
	}


	
}
