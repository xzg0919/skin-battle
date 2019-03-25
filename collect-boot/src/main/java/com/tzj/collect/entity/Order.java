package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import com.tzj.collect.entity.Category.CategoryType;
import org.springframework.transaction.annotation.Transactional;

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
public class Order extends DataEntity<Long> {
	private Long id;
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
	/**
	 * 小区id
	 */
	private Integer communityId;

	@TableField(exist = false)
	private Community community;
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
	private BigDecimal price;
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

	@TableField(exist = false)
	private String cateAttName4Page;// 父类名称/父类名称
	@TableField(exist = false)
	private String num;// 页面需要
	@TableField(exist = false)//成交价格，页面需要
	private String paymentPrice;

	private CategoryType title;// 回收物类型

	private BigDecimal achPrice;//已完成价格
	
	//是否是扫码完成的订单 0不是 1是
	private String isScan;

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

	public CategoryType getTitle() {
		return title;
	}

	public void setTitle(CategoryType title) {
		this.title = title;
	}

	public String getIsCash() {
		return isCash;
	}

	public void setIsCash(String isCash) {
		this.isCash = isCash;
	}

	public String getPaymentPrice() {
		return paymentPrice;
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
			return this.getYMD(this.arrivalTime) + temp;
		}
		return null;
	}

	public void setArrivalTimePage(String arrivalTimePage) {
		this.arrivalTimePage = arrivalTimePage;
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
			statusPage = "待接单";
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
	private OrderItem orderItem;

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

	public OrderType getStatus() {
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

	public OrderItem getOrderItem() {
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

		public Serializable getValue() {
			return this.value;
		}
	}

	@Transactional
	public String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	private String getYMD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);

	}
	

}
