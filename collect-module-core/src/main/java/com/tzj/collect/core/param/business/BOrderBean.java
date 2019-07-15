package com.tzj.collect.core.param.business;

import com.tzj.collect.core.param.ali.OrderItemBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.OrderPic;
import com.tzj.collect.entity.Recyclers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BOrderBean {

	//订单id
		private Integer id;
		 /**
	     * 会员id
	     */
	    private  Integer memberId;
	    
	    /**
	     * 状态 默认是初始状态
	     */
	    private String status;
	    
		private List<Integer> status2;

		/**
	     * 回收企业id
	     */
	    private  Integer companyId;
	    
	    /**
	     * 回收人员id
	     */
	    private  Integer recyclerId;
	    
	    /**
	     * 订单编号
	     */
	    private  String orderNo; 
	    /**
	     * 区域id
	     */
	    private  Integer areaId;
	   
	    /**
	     * 小区id
	     */
	    private  Integer communityId;
	    
	   
	    private Community community;
	    /**
	     * 地址
	     */
	    private  String address;
	    /**
	     * 电话
	     */
	    private  String tel;
	    /**
	     * 联系人
	     */
	    private  String linkMan;
		/**
		 * 订单Id
		 */
		private  String orderId;
	    /**
	     * 分类id
	     */
	    private  Integer categoryId;
	    
	   
	    private Category category;
	    /**
	     * 分类id的parent_ids
	     */
	    private  String categoryParentIds;
	    /**
	     * 基准价
	     */
	    private BigDecimal price;
	    /**
	     * 计量单位
	     */
	    private  String unit;
	    /**
	     * 数量
	     */
	    private  Integer qty;
	    /**
	     * 完成时间
	     */
	    private Date completeDate;
	    /**
	     * 是否评价过
	     */
	    private  String isEvaluated = "0";
	    /**
	     * 取消原因
	     */
	    private String cancelReason;
	    /**
	     * 取消时间
	     */
	    private Date cancelTime;
	    /**
	     * 海域 0为私海
	     */
	    private String level;
	    /**
	     * 上门时间
	     */
	    private Date arrivalTime;
	    /**
	     * 上午am 下午pm
	     */
	    private String arrivalPeriod;
	    /**
	     * 绿账号
	     */
	    private  String greenCode;
	    /**
	     * 阿里userid
	     */
	    private  String aliUserId;
	    /**
	     * 开始时间
	     */
	    private String startTime;
	    /**
	     * 结束时间
	     */
	    private String endTime;
	    
	    //page对象
	    private PageBean pagebean;
	    
	   
	    private OrderItemBean orderItemBean;
	    
	   
	    private OrderPic  OrderPic;
	    
	    private Recyclers recyclers;
	    //是否是扫码完成的订单 0不是 1是
		private String isScan;

		private String categoryType;//订单类型

		public Integer getId() {
			return id;
		}


		public void setId(Integer id) {
			this.id = id;
		}


		public Integer getMemberId() {
			return memberId;
		}


		public void setMemberId(Integer memberId) {
			this.memberId = memberId;
		}


		public String getStatus() {
			return status;
		}


		public void setStatus(String status) {
			this.status = status;
		}


		public List<Integer> getStatus2() {
			return status2;
		}


		public void setStatus2(List<Integer> status2) {
			this.status2 = status2;
		}


		public Integer getCompanyId() {
			return companyId;
		}


		public void setCompanyId(Integer companyId) {
			this.companyId = companyId;
		}


		public Integer getRecyclerId() {
			return recyclerId;
		}


		public void setRecyclerId(Integer recyclerId) {
			this.recyclerId = recyclerId;
		}


		public String getOrderNo() {
			return orderNo;
		}


		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}


		public Integer getAreaId() {
			return areaId;
		}


		public void setAreaId(Integer areaId) {
			this.areaId = areaId;
		}


		public Integer getCommunityId() {
			return communityId;
		}


		public void setCommunityId(Integer communityId) {
			this.communityId = communityId;
		}


		public Community getCommunity() {
			return community;
		}


		public void setCommunity(Community community) {
			this.community = community;
		}


		public String getAddress() {
			return address;
		}


		public void setAddress(String address) {
			this.address = address;
		}


		public String getTel() {
			return tel;
		}


		public void setTel(String tel) {
			this.tel = tel;
		}


		public String getLinkMan() {
			return linkMan;
		}


		public void setLinkMan(String linkMan) {
			this.linkMan = linkMan;
		}


		public Integer getCategoryId() {
			return categoryId;
		}


		public void setCategoryId(Integer categoryId) {
			this.categoryId = categoryId;
		}


		public Category getCategory() {
			return category;
		}


		public void setCategory(Category category) {
			this.category = category;
		}


		public String getCategoryParentIds() {
			return categoryParentIds;
		}


		public void setCategoryParentIds(String categoryParentIds) {
			this.categoryParentIds = categoryParentIds;
		}


		public BigDecimal getPrice() {
			return price;
		}


		public void setPrice(BigDecimal price) {
			this.price = price;
		}


		public String getUnit() {
			return unit;
		}


		public void setUnit(String unit) {
			this.unit = unit;
		}


		public Integer getQty() {
			return qty;
		}


		public void setQty(Integer qty) {
			this.qty = qty;
		}


		public Date getCompleteDate() {
			return completeDate;
		}


		public void setCompleteDate(Date completeDate) {
			this.completeDate = completeDate;
		}


		public String getIsEvaluated() {
			return isEvaluated;
		}


		public void setIsEvaluated(String isEvaluated) {
			this.isEvaluated = isEvaluated;
		}


		public String getCancelReason() {
			return cancelReason;
		}


		public void setCancelReason(String cancelReason) {
			this.cancelReason = cancelReason;
		}


		public Date getCancelTime() {
			return cancelTime;
		}


		public void setCancelTime(Date cancelTime) {
			this.cancelTime = cancelTime;
		}


		public String getLevel() {
			return level;
		}


		public void setLevel(String level) {
			this.level = level;
		}


		public Date getArrivalTime() {
			return arrivalTime;
		}


		public void setArrivalTime(Date arrivalTime) {
			this.arrivalTime = arrivalTime;
		}


		public String getArrivalPeriod() {
			return arrivalPeriod;
		}


		public void setArrivalPeriod(String arrivalPeriod) {
			this.arrivalPeriod = arrivalPeriod;
		}


		public String getGreenCode() {
			return greenCode;
		}


		public void setGreenCode(String greenCode) {
			this.greenCode = greenCode;
		}


		public String getAliUserId() {
			return aliUserId;
		}


		public void setAliUserId(String aliUserId) {
			this.aliUserId = aliUserId;
		}


		public PageBean getPagebean() {
			return pagebean;
		}


		public void setPagebean(PageBean pagebean) {
			this.pagebean = pagebean;
		}


		public OrderItemBean getOrderItemBean() {
			return orderItemBean;
		}


		public void setOrderItemBean(OrderItemBean orderItemBean) {
			this.orderItemBean = orderItemBean;
		}


		public OrderPic getOrderPic() {
			return OrderPic;
		}


		public void setOrderPic(OrderPic orderPic) {
			OrderPic = orderPic;
		}


		public Recyclers getRecyclers() {
			return recyclers;
		}


		public void setRecyclers(Recyclers recyclers) {
			this.recyclers = recyclers;
		}


		public String getStartTime() {
			return startTime;
		}


		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}


		public String getEndTime() {
			return endTime;
		}


		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getIsScan() {
			return isScan;
		}


		public void setIsScan(String isScan) {
			this.isScan = isScan;
		}

		public String getCategoryType() {
			return categoryType;
		}

		public void setCategoryType(String categoryType) {
			this.categoryType = categoryType;
		}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
