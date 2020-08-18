package com.tzj.collect.entity;

import com.alipay.api.domain.OrderItem;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 订单表
 *
 * @Author 王灿
 **/
/**
 * @author Administrator
 *
 */
@TableName("sb_order")
@Data
public class Order extends DataEntity<Long> {
	private Long id;

	private String complaintType;//客诉类型  0催派 1催接 2催收  3形成客诉

	private String voucherMemberId;

	private String isItemAch;//是否编辑过完成重量

	/**
	 * 会员id
	 */
	private Integer memberId;

	@TableField(exist = false)
	private Member memeber;
	/**
	 * 回收企业id
	 */
	private Integer companyId;

	@TableField(exist = false)
	private Company company;
	/**
	 * 回收人员id
	 */
	private Integer recyclerId;

	/**
	 * 订单状态
	 */
	@TableField(value = "status_")
	private OrderType status = OrderType.INIT;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 区域id
	 */
	private Integer areaId;
	/**
	 * 街道id
	 */
	private Integer streetId;

	@TableField(exist = false)
	private Area area;
	@TableField(exist = false)
	private String quantity;
	/**
	 * 小区id
	 */
	private Integer communityId;

	@TableField(exist = false)
	private Community community;
	@TableField(exist = false)
	private Integer overTime;
	@TableField(exist = false)
	private Integer overTimes;
	@TableField(exist = false)
	private String examineReason;
	@TableField(exist = false)
	private String examineStatus;
	@TableField(exist = false)
	private String isComplaint;
	@TableField(exist = false)
	private String money;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 联系人
	 */
	private String linkMan;
	/**
	 * 分类id
	 */
	private Integer categoryId;

	@TableField(exist = false)
	private Category category;
	/**
	 * 分类id的parent_ids
	 */
	private String categoryParentIds;
	/**
	 * 基准价
	 */
	private BigDecimal price=new BigDecimal("0");
	/**
	 * 计量单位
	 */
	private String unit;
	/**
	 * 数量
	 */
	private Integer qty;
	/**
	 * 完成时间
	 */
	private Date completeDate;
	/**
	 * 是否评价过 0 未评价 1 已评价
	 */
	private String isEvaluated = "0";
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

	private String fullAddress;// 详细住址

	private BigDecimal commissionsPrice;//佣金价格

	private BigDecimal adminCommissions;//平台佣金系数

	private BigDecimal companyCommissions;//服务商返佣系数

	private BigDecimal backCommissionsPrice;//返佣

	private String taobaoNo; //咸鱼外部订单号

	@TableField(exist = false)
	private String cateAttName4Page;// 父类名称/父类名称
	@TableField(exist = false)
	private String num;// 页面需要
	@TableField(exist = false)
	private String isOverTimes;// 1-已超时
	@TableField(exist = false)//成交价格，页面需要
	private String paymentPrice;
	@TableField(exist = false)//页面需要
	private CompanyEquipment companyEquipment;

	private TitleType title;// 回收物类型

	private BigDecimal achPrice;//已完成价格

	private BigDecimal discountPrice;//优惠价格

	//是否是扫码完成的订单 0不是 1是
	private String isScan;
	//是否读取0未读 1以读取
	private String isRead="0";

	private Double greenCount;

	private String enterpriseCode;//以旧换新码
	/**
	 * 快递总重量
	 */
	private BigDecimal expressAmount;
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
	private Integer logisticsId;
	/**
	 * 物流公司名称
	 */
	private String logisticsName;

	/**
	 * 是否授权蚂蚁森林能量 0 不需要 1需要
	 */
	private String isMysl;
	/*
	蚂蚁深林能量Id
	 */
	private String myslOrderId;

	private String myslParam;

	private String iotEquipmentCode;//iot设备编号

	/**
	 * 订单备注
	 */
	private String orderRemarks;

	private String isRisk;

	private String formId;


	private BigDecimal priceT;

	private Integer cleanUp = 1; //生活垃圾-是否平铺整理 1-否 2-是

	@TableField(exist = false)//页面需要
	private String doublePoint;//双倍积分奖励  “greenCount/2 * 2”
	@TableField(exist = false)//页面需要
	private String itemType;//回收类型

	private Integer netId;//回收点id

	private String netName;//回收点名称

	private String payType;//结算方式 0-卖钱 1-环保积分 2-能量

	private String orderFrom = "0";//订单来源0-其他 1-定时定点  2闲鱼

	private String paymentType;//付款类型 0支付宝    1现金
	@TableField(value = "company_point")
	private double companyPoint;
	@TableField(exist = false)//页面需要
	private String categoryTitle;// 垃圾类型

	private String aliAccount;//支付宝账号


	public String getDoublePoint() {
		if (2==cleanUp&&null!=greenCount){
			return greenCount/2+"*2";
		}
		return doublePoint;
	}

	public void setDoublePoint(String doublePoint) {
		this.doublePoint = doublePoint;
	}

	/**
	 * 是否待支付
	 * @return
	 */
	@TableField(exist = false)
	private String isPayment;

	public String getIsPayment() {
		if((title+"").equals(TitleType.BIGTHING+"")&&(status+"").equals(OrderType.ALREADY+"")&&achPrice.compareTo((new BigDecimal("0")))==1){
			return "Y";
		}
		return "N";
	}

	public String getMoney() {
		try {
			if (StringUtils.isBlank(money)){
				return achPrice.subtract(discountPrice).abs().toString();
			}
		}catch (Exception e){

		}
		return null;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getIsRisk() {
		return isRisk;
	}

	public void setIsRisk(String isRisk) {
		this.isRisk = isRisk;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getMyslOrderId() {
		return myslOrderId;
	}

	public void setMyslOrderId(String myslOrderId) {
		this.myslOrderId = myslOrderId;
	}

	public String getMyslParam() {
		return myslParam;
	}

	public void setMyslParam(String myslParam) {
		this.myslParam = myslParam;
	}

	public String getOrderRemarks() {
		return orderRemarks;
	}

	public void setOrderRemarks(String orderRemarks) {
		this.orderRemarks = orderRemarks;
	}

	public void setIsPayment(String isPayment) {
		this.isPayment = isPayment;
	}

	public String getIsMysl() {
		return isMysl;
	}

	public void setIsMysl(String isMysl) {
		this.isMysl = isMysl;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public Integer getStreetId() {
		return streetId;
	}

	public void setStreetId(Integer streetId) {
		this.streetId = streetId;
	}

	public BigDecimal getExpressAmount() {
		return expressAmount;
	}

	public void setExpressAmount(BigDecimal expressAmount) {
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

	public Integer getLogisticsId() {
		return logisticsId;
	}

	public void setLogisticsId(Integer logisticsId) {
		this.logisticsId = logisticsId;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public BigDecimal getAchPrice() {
		//return achPrice;
		if(achPrice!=null) {
			return achPrice.setScale(2,BigDecimal.ROUND_DOWN);
		}
		return null;
	}

	public void setAchPrice(BigDecimal achPrice) {
		this.achPrice = achPrice;
	}

	private String isCash;//是否以现金支付

	private String achRemarks;//订单完成备注
	/**
	 * 订单完成时,用户的签名的图片链接
	 */
	private String signUrl;

	/**
	 * 接单时间
	 */
	private Date receiveTime;

	public Object getReceiveTime() {
		if(null!=receiveTime) {
			return this.getDate(receiveTime);
		}
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Object getDistributeTime() {
		if(null!=distributeTime) {
			return this.getDate(distributeTime);
		}
		return distributeTime;
	}

	public void setDistributeTime(Date distributeTime) {
		this.distributeTime = distributeTime;
	}

	public String getCateAttName4Page() {
		return cateAttName4Page;
	}

	public void setCateAttName4Page(String cateAttName4Page) {
		this.cateAttName4Page = cateAttName4Page;
	}

	public TitleType getTitle() {
		return title;
	}

	public void setTitle(TitleType title) {
		this.title = title;
	}

	public String getIsCash() {
		return isCash;
	}

	public void setIsCash(String isCash) {
		this.isCash = isCash;
	}

	public String getPaymentPrice() {
		if (null != paymentPrice){
			return ApiStringUtils.doublegetTwoDecimal(Float.parseFloat(paymentPrice));
		}else if (null != achPrice){
			return ApiStringUtils.doublegetTwoDecimal(achPrice.floatValue());
		}
		return ApiStringUtils.doublegetTwoDecimal(0f);
	}

	public void setPaymentPrice(String paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	/**
	 * 派单时间
	 */
	private Date distributeTime;
	// 是否被派送过
	private String isDistributed;

	/**
	 * 上午am 下午pm
	 */
	private String arrivalPeriod;
	/**
	 * 绿账号
	 */
	private String greenCode;
	/**
	 * 阿里userid
	 */
	private String aliUserId;

	@TableField(exist = false)
	private Recyclers recyclers;

	/**
	 * 返回下单时间(只用于页面需求)
	 */
	@TableField(exist = false)
	private String createDatePage;

	public String getCreateDatePage() {
		return this.getDate(this.createDate);
	}

	public void setCreateDatePage(String createDatePage) {
		this.createDatePage = createDatePage;
	}

	/**
	 * 返回上门时间(只用于页面需求)
	 */
	@TableField(exist = false)
	private String arrivalTimePage;

	public String getArrivalTimePage() {
		String temp = this.arrivalPeriod;
		if (temp != null && !"".equals(temp)) {
			if (temp.equals("am")) {
				temp = "上午";
			} else if (temp.equals("pm")) {
				temp = "下午";
			}
		}
		if (this.arrivalTime != null) {
			return this.getYMD(this.arrivalTime) +" "+ temp;
		}
		return null;
	}

	public void setArrivalTimePage(String arrivalTimePage) {
		this.arrivalTimePage = arrivalTimePage;
	}

	@TableField(exist = false)
	private String isReOrder; //是否为再处理订单 1-是 0-否

	public String getIsReOrder() {
		String status = this.status.toString();
		String temp = this.cancelReason;
		if ("INIT".equals(status)){
			if (null != temp && !"订单回调".equals(temp)) {
				return "1";
			}else{
				return "0";
			}
		}else{
			return null;
		}
	}

	public void setIsReOrder(String isReOrder) {
		this.isReOrder = isReOrder;
	}

	/**
	 * 预约时间(只用于页面需求)
	 */
	@TableField(exist = false)
	private String arrivalPage;

	public String getArrivalPage() {
		if (this.arrivalTime != null) {
			return this.getDate(this.arrivalTime);
		}
		return null;
	}

	public void setArrivalPage(String arrivalPage) {
		this.arrivalPage = arrivalPage;
	}

	/**
	 * 返回状态时间(只用于页面需求)
	 */
	@TableField(exist = false)
	private String datePage;

	public String getDatePage() {
		try {
			switch (this.status) {
				case INIT:
					datePage = createDate != null ? this.getDate(this.createDate) : "";
					break;
				case COMPLETE:
					datePage = completeDate != null ? this.getDate(this.completeDate) : "";
					break;
				case CANCEL:
					datePage = cancelTime != null ? this.getDate(this.cancelTime) : "";
					break;
				case TOSEND:
					datePage = distributeTime != null ? this.getDate(this.distributeTime) : "";
					break;
				case ALREADY:
					datePage = receiveTime != null ? this.getDate(this.receiveTime) : "";
					break;
				case REJECTED:
					datePage = cancelTime != null ? this.getDate(this.cancelTime) : "";
					break;
				default:
					break;
			}
		} catch (Exception e) {
			return "返回的时间有误";
		}

		return datePage;
	}

	public void setDatePage(String datePage) {
		this.datePage = datePage;
	}

	/**
	 * 返回状态时间(只用于页面需求)
	 */
	@TableField(exist = false)
	private String statusDatePage;

	public String getStatusDatePage() {
		try {
			switch (this.status) {
				case INIT:
					statusDatePage = "下单时间: " + this.getDate(this.createDate);
					break;
				case COMPLETE:
					statusDatePage = "完成时间: " + this.getDate(this.completeDate);
					break;
				case CANCEL:
					statusDatePage = "取消时间: " + this.getDate(this.cancelTime);
					break;
				case TOSEND:
					statusDatePage = "下单时间: " + this.getDate(this.createDate);
					break;
				case ALREADY:
					statusDatePage = "下单时间: " + this.getDate(this.createDate);
					break;
				case REJECTED:
					statusDatePage = "取消时间: " + this.getDate(this.cancelTime);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			return "返回的时间有误";
		}
		return statusDatePage;
	}

	public void setStatusDatePage(String statusDatePage) {
		this.statusDatePage = statusDatePage;
	}

	/**
	 * 返回具体的状态(只用于页面需求)
	 */
	@TableField(exist = false)
	private String statusPage;

	public String getStatusPage() {
		switch (this.status) {
			case INIT:
				statusPage = "待接单";
				break;
			case COMPLETE:
				statusPage = "已完成";
				break;
			case CANCEL:
				statusPage = "已取消";
				break;
			case TOSEND:
				// statusPage = "已派单";
				statusPage = "已派发";
				break;
			case ALREADY:
				statusPage = "进行中";
				break;
			case REJECTED:
				statusPage = "平台已取消";
				break;
			default:
				break;
		}
		return statusPage;
	}

	public void setStatusPage(String statusPage) {
		this.statusPage = statusPage;
	}

	@TableField(exist = false)
	private com.alipay.api.domain.OrderItem orderItem;

	@TableField(exist = false)
	private OrderPic OrderPic;// orderLog

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Member getMemeber() {
		return memeber;
	}

	public void setMemeber(Member memeber) {
		this.memeber = memeber;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Integer getRecyclerId() {
		return recyclerId;
	}

	public void setRecyclerId(Integer recyclerId) {
		this.recyclerId = recyclerId;
	}

	public OrderType  getStatus() {
		return status;
	}

	public void setStatus(OrderType status) {
		this.status = status;
	}

	// 公海和私海的区分
	public String getStatus4Page() {
//		if (OrderType.ALREADY.equals(status) && "1".equals(level)) {
//			return "distribute";
//		}
		return status.toString();
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

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
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
		return price.setScale(2,BigDecimal.ROUND_DOWN);
	}

	public String getPrice4Page(){
		return ApiStringUtils.doublegetTwoDecimal(price.floatValue());
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

	public Object getCompleteDate() {
		if(null!=completeDate) {
			return this.getDate(completeDate);
		}
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

	public Object getCancelTime() {
		if(null!=cancelTime) {
			return this.getDate(cancelTime);
		}
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

	public com.alipay.api.domain.OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
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

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getIsDistributed() {
		return isDistributed;
	}

	public void setIsDistributed(String isDistributed) {
		this.isDistributed = isDistributed;
	}

	public String getAchRemarks() {
		return achRemarks;
	}

	public void setAchRemarks(String achRemarks) {
		this.achRemarks = achRemarks;
	}

	public String getSignUrl() {
		return signUrl;
	}

	public void setSignUrl(String signUrl) {
		this.signUrl = signUrl;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getIsScan() {
		return isScan;
	}

	public void setIsScan(String isScan) {
		this.isScan = isScan;
	}

	public Double getGreenCount() {
		return greenCount;
	}

	public void setGreenCount(Double greenCount) {
		this.greenCount = greenCount;
	}

	public enum TitleType implements IEnum{
		DEFUALT(0),   	 //初始值
		DIGITAL(1),		//家电数码
		HOUSEHOLD(2),	//生活垃圾
		FIVEKG(3),		//5公斤废纺衣物回收
		BIGTHING(4),	//大件垃圾
		IOTORDER(5), // iot设备
		IOTCLEANORDER(6),//iot清运订单
		HOUSEDIGITAL(7);//生活垃圾/废弃家电
		private int value;

		TitleType(final int value) {
			this.value = value;
		}

		@Override
		public Serializable getValue() {
			return this.value;
		}
	}

	public enum OrderType implements IEnum {
		INIT(0), // 待接单
		TOSEND(1), // 已派送
		ALREADY(2), // 已接单
		COMPLETE(3), // 已完成
		CANCEL(4), // 已取消
		REJECTED(5), // 驳回
		CANCELTASK(6);// 取消任务仅用于日志
		private int value;

		OrderType(final int value) {
			this.value = value;
		}

		@Override
		public Serializable getValue() {
			return this.value;
		}
	}

	public String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	private String getYMD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);

	}

	public String getIotEquipmentCode() {
		return iotEquipmentCode;
	}

	public void setIotEquipmentCode(String iotEquipmentCode) {
		this.iotEquipmentCode = iotEquipmentCode;
	}

	public CompanyEquipment getCompanyEquipment() {
		return companyEquipment;
	}

	public void setCompanyEquipment(CompanyEquipment companyEquipment) {
		this.companyEquipment = companyEquipment;
	}
}
