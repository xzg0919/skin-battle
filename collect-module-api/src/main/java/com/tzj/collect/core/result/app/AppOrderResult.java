package com.tzj.collect.core.result.app;

import com.baomidou.mybatisplus.annotations.TableField;
import com.tzj.collect.entity.Category.CategoryType;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
@Data
public class AppOrderResult {

	private String isItemAch;//0没有  1有
	
	private String orderId;//订单id
	
	private String address;//地址
	
	private String communityId;//小区Id
	
	private String companyId;//企业Id
	
	private String fullAddress;//详细地址
	
	private String creatDate;//订单创建时间
	
	private String orderNo;//订单编号
	
	private String price;//预计回收价格
	
	private String achPrice;//实际回收价格
	
	private String linkName;//联系人姓名
	
	private String status;//状态
	
	private String cateName;//商品名称
	
	private String tel;//电话
	
	private String icon;//图片

	private String signUrl;
	
	private String remarks;//描述

	private  String createDate;
	
	private String arrivalTime;//预计到达时间

	private  String cancelTime;
	private  String receiveTime;
	private  String distributeTime;
	private  String completeDate;
	private  String isRead;
	private String orderRemarks;
	private String aliUserId;


	private String arrivalPeriod;//时间段
	
	private String completeTime;//完成时间
	
	private String comTel;//公司电话
	
	private String comName;//公司名称
	
	private Object greenCount;//绿色能量
	
	private List<AttrItem> attrItemlist;//分类名称
	
	private List<AttrItem> orderUrlList;//图片列表
	
	private String currentNum;//当前订单数量
	
	private String currentSum;//当前订单总金额
	
	private String countNum;//总金额
	
	private String total;//总订单（都是已完成）
	
	private String currentTime;//當前時間

	private CategoryType title;//最上层分类
	
	private String cateAttName4Page;// 父类名称/父类名称
	
	private String achRemarks;//完成回收物描述
	
	private List<Map<String, Object>> priceAndAmount;
	
	private String longitude;//经度
	
	private String latitude;//纬度
	
	private String isCash;//是否免费

	private String isManager;//是否是经理

	private Object paymentPrice;//成交价格

	private String paymentNo;

	private String cancelReason;

	private Object obj;

	private String isTenGreen; //0不是  1是

	private String overTime;//超时时间

	private String overTimes;//超时时间

	private String isComplaint;

	private String complaintType;//客诉类型  0催派 1催接 2催收  3形成客诉


	public String getAliUserId() {
		return aliUserId;
	}

	public void setAliUserId(String aliUserId) {
		this.aliUserId = aliUserId;
	}

	public String getSignUrl() {
		return signUrl;
	}

	public void setSignUrl(String signUrl) {
		this.signUrl = signUrl;
	}

	public String getOrderRemarks() {
		return orderRemarks;
	}

	public void setOrderRemarks(String orderRemarks) {
		this.orderRemarks = orderRemarks;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getDistributeTime() {
		return distributeTime;
	}

	public void setDistributeTime(String distributeTime) {
		this.distributeTime = distributeTime;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getCreatDate() {
		//return creatDate;
		if (creatDate != null && !"".equals(creatDate)) {
			return getDate(creatDate);
		}
		return creatDate;
	}

	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAchPrice() {
		return achPrice;
	}

	public void setAchPrice(String achPrice) {
		this.achPrice = achPrice;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getArrivalPeriod() {
		return arrivalPeriod;
	}

	public void setArrivalPeriod(String arrivalPeriod) {
		this.arrivalPeriod = arrivalPeriod;
	}

	public String getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}

	public String getComTel() {
		return comTel;
	}

	public void setComTel(String comTel) {
		this.comTel = comTel;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public List<AttrItem> getAttrItemlist() {
		return attrItemlist;
	}

	public void setAttrItemlist(List<AttrItem> attrItemlist) {
		this.attrItemlist = attrItemlist;
	}

	public List<AttrItem> getOrderUrlList() {
		return orderUrlList;
	}

	public void setOrderUrlList(List<AttrItem> orderUrlList) {
		this.orderUrlList = orderUrlList;
	}

	public String getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(String currentNum) {
		this.currentNum = currentNum;
	}

	public String getCurrentSum() {
		return currentSum;
	}

	public void setCurrentSum(String currentSum) {
		this.currentSum = currentSum;
	}

	public String getCountNum() {
		return countNum;
	}

	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	
	public CategoryType getTitle() {
		return title;
	}

	public void setTitle(CategoryType title) {
		this.title = title;
	}

	public String getCateAttName4Page() {
		return cateAttName4Page;
	}

	public void setCateAttName4Page(String cateAttName4Page) {
		this.cateAttName4Page = cateAttName4Page;
	}

	
	public List<Map<String, Object>> getPriceAndAmount() {
		return priceAndAmount;
	}

	public void setPriceAndAmount(List<Map<String, Object>> priceAndAmount) {
		this.priceAndAmount = priceAndAmount;
	}

	//	public String getDate(Date date) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		return sdf.format(date);
//	}
//	
	public String getDate(String date) {
		String s = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			s =  sdf.format(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!"".equals(s)) {
			return s;
		}
		return null;
	}
	
	public String getArrivalTimePeriod() {
		StringBuilder builder = null;
		if (arrivalTime != null && !"".equals(arrivalTime)) {
			builder = new StringBuilder(arrivalTime);
		}else{
			return "";
		}
		String temp = this.arrivalPeriod;
		if (temp != null && !"".equals(temp)) {
			if (temp.equals("pm")) {
				temp = "下午";
			}else if (temp.equals("am")) {
				temp = "上午";
			}
			return builder.append(" "+temp).toString();
		}
		return arrivalTime;
	}
	
	public String getAddressFull() {
		if (address == null || "".equals(address)) {
			return address;
		}
		StringBuilder builder = new StringBuilder(address);
		if (this.fullAddress != null && !"".equals(fullAddress)) {
			builder.append(fullAddress);
		}
		return builder.toString();
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getAchRemarks() {
		return achRemarks;
	}

	public void setAchRemarks(String achRemarks) {
		this.achRemarks = achRemarks;
	}

	public Object getGreenCount() {
		return greenCount;
	}

	public void setGreenCount(Object greenCount) {
		this.greenCount = greenCount;
	}

	public String getIsCash() {
		return isCash;
	}

	public void setIsCash(String isCash) {
		this.isCash = isCash;
	}

	public Object getPaymentPrice() {
		return paymentPrice;
	}

	public void setPaymentPrice(Object paymentPrice) {
		this.paymentPrice = paymentPrice;
	}
}
