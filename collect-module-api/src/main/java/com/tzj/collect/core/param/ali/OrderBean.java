package com.tzj.collect.core.param.ali;


import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.OrderPic;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 前台订单对象
 * @author Michael_Wang
 *
 */

@Data
public class OrderBean {
    public String getDingDingSing() {
        return dingDingSing;
    }

    public void setDingDingSing(String dingDingSing) {
        this.dingDingSing = dingDingSing;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAchRemarks() {
        return achRemarks;
    }

    public void setAchRemarks(String achRemarks) {
        this.achRemarks = achRemarks;
    }

    public Integer getCategoryParentId() {
        return categoryParentId;
    }

    public void setCategoryParentId(Integer categoryParentId) {
        this.categoryParentId = categoryParentId;
    }

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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsOverTime() {
        return isOverTime;
    }

    public void setIsOverTime(String isOverTime) {
        this.isOverTime = isOverTime;
    }

    public String getRecyclerName() {
        return recyclerName;
    }

    public void setRecyclerName(String recyclerName) {
        this.recyclerName = recyclerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsBig() {
        return isBig;
    }

    public void setIsBig(String isBig) {
        this.isBig = isBig;
    }

    public String getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(String isDelivery) {
        this.isDelivery = isDelivery;
    }

    public String getIsComplaint() {
        return isComplaint;
    }

    public void setIsComplaint(String isComplaint) {
        this.isComplaint = isComplaint;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getIsNormal() {
        return isNormal;
    }

    public void setIsNormal(String isNormal) {
        this.isNormal = isNormal;
    }

    public Integer getCleanUp() {
        return cleanUp;
    }

    public void setCleanUp(Integer cleanUp) {
        this.cleanUp = cleanUp;
    }

    public Integer getNetId() {
        return netId;
    }

    public void setNetId(Integer netId) {
        this.netId = netId;
    }

    public String getAliAccount() {
        return aliAccount;
    }

    public void setAliAccount(String aliAccount) {
        this.aliAccount = aliAccount;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getOrderRemarks() {
        return orderRemarks;
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
    }

    public Integer getStreetId() {
        return streetId;
    }

    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
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

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
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

    public String getAchPrice() {
        return achPrice;
    }

    public void setAchPrice(String achPrice) {
        this.achPrice = achPrice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getIsCash() {
        return isCash;
    }

    public void setIsCash(String isCash) {
        this.isCash = isCash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PageBean getPagebean() {
        return pagebean;
    }

    public void setPagebean(PageBean pagebean) {
        this.pagebean = pagebean;
    }

    public String getDingDingUrl() {
        return dingDingUrl;
    }

    public void setDingDingUrl(String dingDingUrl) {
        this.dingDingUrl = dingDingUrl;
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

    public OrderItemBean getOrderItemBean() {
        return orderItemBean;
    }

    public void setOrderItemBean(OrderItemBean orderItemBean) {
        this.orderItemBean = orderItemBean;
    }

    public com.tzj.collect.entity.OrderPic getOrderPic() {
        return OrderPic;
    }

    public void setOrderPic(com.tzj.collect.entity.OrderPic orderPic) {
        OrderPic = orderPic;
    }

    public String getComplaintBack() {
        return complaintBack;
    }

    public void setComplaintBack(String complaintBack) {
        this.complaintBack = complaintBack;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<IdAmountListBean> getIdAndListList() {
        return idAndListList;
    }

    public void setIdAndListList(List<IdAmountListBean> idAndListList) {
        this.idAndListList = idAndListList;
    }

    public List<OrderItemBean> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemBean> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getExpressAmount() {
        return expressAmount;
    }

    public void setExpressAmount(String expressAmount) {
        this.expressAmount = expressAmount;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressTel() {
        return expressTel;
    }

    public void setExpressTel(String expressTel) {
        this.expressTel = expressTel;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getIsMysl() {
        return isMysl;
    }

    public void setIsMysl(String isMysl) {
        this.isMysl = isMysl;
    }

    public String getIsRisk() {
        return isRisk;
    }

    public void setIsRisk(String isRisk) {
        this.isRisk = isRisk;
    }

    public String getMyslParam() {
        return myslParam;
    }

    public void setMyslParam(String myslParam) {
        this.myslParam = myslParam;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    private String dingDingSing;

    private String discountPrice;

    private String voucherId;

    private Integer orderId;

	//用来储存订单描述
	private String remarks;
	
	private String achRemarks;//完成订单描述
	/**
	 * 分类父级Id
	 */
	private Integer categoryParentId;
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
    /**
     * 行政区Id
     */
    private String cityId;

    private String parentId;

	private List<Integer> status2;

	/**
     * 回收企业id
     */
    private  Integer companyId;
    
    private String companyName;
    
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
	 *
	 */
	private String orderRemarks;
    /**
     * 街道Id
     */
    private  Integer streetId;
    /**
     * 小区id
     */
    private  Integer communityId;
    
   
    private Community community;
    /**
     * 地址
     */
    private  String address;
    
    private String fullAddress;
    /**
     * 电话
     */
    private  String tel;
    /**
     * 联系人
     */
    private  String linkMan;
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
    private String arrivalTime;
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
     * 完成价格
     */
    private  String achPrice;
    /**
     * 重量
     */
    private  double amount;
    /**
     * 是否是免费 0是不免费，1是免费
     */
    private  String isCash;
	/**
	 * 类型
	 */
	private  String type;
    
    //page对象
    private PageBean  pagebean;
    private String dingDingUrl;   //钉钉通知的连接

	private String startTime;
	private String endTime;
   
    private OrderItemBean  orderItemBean;
    
   
    private OrderPic  OrderPic;

    private String complaintBack;//反馈客诉内容
    
    private String title;//回收无类型
    
    private List<IdAmountListBean> idAndListList;//构造参数
    
    private List<OrderItemBean> orderItemList;

    private String resultStatus;//交易是否成功
    
    /**
	 * 订单完成时,用户的签名的图片链接
	 */
	private String signUrl;

	private String enterpriseCode;//以旧换新码

	private String picUrl;

	private String special;

	/**
	 * 快递总重量
	 */
	private String expressAmount;
	/**
	 * 快递单号
	 */
	private String expressNo;
	/**
	 * 快递员姓名
	 */
	private String expressName;
	/**
	 * 快递员电话
	 */
	private String expressTel;
	/**
	 * 第三方物流公司ID
	 */
	private String logisticsId;

	/**
	 * 是否授权蚂蚁森林能量仅仅用于页面展示
	 */
	private String isMysl;

	private String isRisk;

	private String myslParam;
	
	private String formId;

	private String templateId;

	private String OrderNo;

	private String linkName;
    /**
     * 经纬度
     */
    private String location;

    private String isOverTime;//是否超时0，不超时  1超时

    private String recyclerName;//

    private String mobile;//

    private String isBig;//"Y"是大件 其他情况不是

    private String isDelivery;//马上回收（每笔订单是否派送红包）

    private String isComplaint; //是否客诉

    private String reason;

    private String complaintType;//客诉类型  0催派 1催接 2催收  3形成客诉

    private String shareCode;//线上线下分享码

    private String isNormal = "0";//是否是闲鱼正常订单0是，1不是

    private  Integer cleanUp; // 是否平铺整理  1-否 2-是

    private Integer netId;//回收点id

    private String aliAccount;

    private Integer operatorId;//操作员编号

    private String operatorName;//操作员

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    private String orderFrom;  //订单来源


    String amPm;


    Integer clothesCount;
    String orderDate;



}
