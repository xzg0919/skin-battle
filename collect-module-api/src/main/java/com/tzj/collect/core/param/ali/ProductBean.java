package com.tzj.collect.core.param.ali;

import com.baomidou.mybatisplus.annotations.TableField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created on 2018-08-08
 * <p>Description: [商品表Entity]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [wangcan]
 * @version        1.0
 */
public class ProductBean 
{
	/**
     *  用户姓名
     */
    private String userName;
    /**
     *  手机号
     */
    private String mobile;
	
	/**
     *  商品类型
     */
    private String id;

    /**
     *  商品类型
     */
    private String productType;

    /**
     *  优惠券类型
     */
    private String voucherType;

    /**
     *  兑换所需积分
     */
    private Integer bindingPoint;

    /**
     *  兑换所需金额
     */
    private BigDecimal bindingPrice;

    /**
     *  优惠券面额
     */
    private BigDecimal amount;

    /**
     *  商品图片
     */
    private String img;

    /**
     *  最低消费金额
     */
    private BigDecimal lowLimit;

    /**
     *  商品描述
     */
    private String content;

    /**
     *  商品编号
     */
    private String sn;

    /**
     *  已兑换数量
     */
    private Integer bindingQuantity;
    /**
     *  订单ID
     */
    private String orderId;

    /**
     *  商品名称
     */
    private String name;

    /**
     *  商品副名称
     */
    private String subName;

    /**
     *  商品原价
     */
    private BigDecimal oldPrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 是否上架
     */
    private String isMarketable;

    /**
     *  领取开始日期
     */
    private Date pickStartDate;

    /**
     *  领取结束日期
     */
    private Date pickEndDate;

    /**
     *  有效日期类型
     */
    private String validType;

    /**
     *  领取后第n天
     */
    private String validFromDay;

    /**
     *  领取后第m天
     */
    private String validToDay;

    /**
     *  有效期开始日期
     */
    private Date validStartDate;

    /**
     *  有效期结束日期
     */
    private Date validEndDate;

    /**
     *  领取限制周期：天周月
     */
    private String pickLimitPeriod;

    /**
     *  领取限制周期内最多领取数量
     */
    private String pickLimitNum;

    /**
     *  最多领取数量
     */
    private String pickLimitTotal;

    /**
     *  周几可用
     */
    private String vaildWeek;

    /**
     *  可用时间
     */
    private String vaildTime;

    /**
     *  不可使用日期
     */
    private String unusedableDate;

    /**
     * 用于查询时间段
     */
    @TableField(exist=false)
    private Date now;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 区id
     */
    private String districtsId;

    /**
     * 行业分类
     */
    private String shopCategory;
    
    /**
     * 门店地址
     */
    private String address;
    /**
     * 预售商品1:是
     */
    private String preSell;
    /**
     * 实际开始领取日期
     */
    private Date realPickupDate;
    /**
     * 外部券URL
     */
    private String  outURL;
    
    /**
     * 绿账商家主键
     */
    private String  greenMerchantId;
    /**
     * 归属(家乐福等等)
     */
    private String belong;
    
    
    /**
    * @Fields field:field:【组合券张数】
    */
    private Integer combinationCount;
    
    /**
     * 商品类型
     */
    private String goodsType;
    /**
     * 行政区Id
     */
    private String cityId;

	public String getGoodsType() {
		return goodsType;
	}


	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getProductType() {
		return productType;
	}


	public void setProductType(String productType) {
		this.productType = productType;
	}


	public String getVoucherType() {
		return voucherType;
	}


	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}


	public Integer getBindingPoint() {
		return bindingPoint;
	}


	public void setBindingPoint(Integer bindingPoint) {
		this.bindingPoint = bindingPoint;
	}


	public BigDecimal getBindingPrice() {
		return bindingPrice;
	}


	public void setBindingPrice(BigDecimal bindingPrice) {
		this.bindingPrice = bindingPrice;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}


	public BigDecimal getLowLimit() {
		return lowLimit;
	}


	public void setLowLimit(BigDecimal lowLimit) {
		this.lowLimit = lowLimit;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getSn() {
		return sn;
	}


	public void setSn(String sn) {
		this.sn = sn;
	}


	public Integer getBindingQuantity() {
		return bindingQuantity;
	}


	public void setBindingQuantity(Integer bindingQuantity) {
		this.bindingQuantity = bindingQuantity;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSubName() {
		return subName;
	}


	public void setSubName(String subName) {
		this.subName = subName;
	}


	public BigDecimal getOldPrice() {
		return oldPrice;
	}


	public void setOldPrice(BigDecimal oldPrice) {
		this.oldPrice = oldPrice;
	}


	public Integer getStock() {
		return stock;
	}


	public void setStock(Integer stock) {
		this.stock = stock;
	}


	public String getIsMarketable() {
		return isMarketable;
	}


	public void setIsMarketable(String isMarketable) {
		this.isMarketable = isMarketable;
	}


	public Date getPickStartDate() {
		return pickStartDate;
	}


	public void setPickStartDate(Date pickStartDate) {
		this.pickStartDate = pickStartDate;
	}


	public Date getPickEndDate() {
		return pickEndDate;
	}


	public void setPickEndDate(Date pickEndDate) {
		this.pickEndDate = pickEndDate;
	}


	public String getValidType() {
		return validType;
	}


	public void setValidType(String validType) {
		this.validType = validType;
	}


	public String getValidFromDay() {
		return validFromDay;
	}


	public void setValidFromDay(String validFromDay) {
		this.validFromDay = validFromDay;
	}


	public String getValidToDay() {
		return validToDay;
	}


	public void setValidToDay(String validToDay) {
		this.validToDay = validToDay;
	}


	public Date getValidStartDate() {
		return validStartDate;
	}


	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}


	public Date getValidEndDate() {
		return validEndDate;
	}


	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}


	public String getPickLimitPeriod() {
		return pickLimitPeriod;
	}


	public void setPickLimitPeriod(String pickLimitPeriod) {
		this.pickLimitPeriod = pickLimitPeriod;
	}


	public String getPickLimitNum() {
		return pickLimitNum;
	}


	public void setPickLimitNum(String pickLimitNum) {
		this.pickLimitNum = pickLimitNum;
	}


	public String getPickLimitTotal() {
		return pickLimitTotal;
	}


	public void setPickLimitTotal(String pickLimitTotal) {
		this.pickLimitTotal = pickLimitTotal;
	}


	public String getVaildWeek() {
		return vaildWeek;
	}


	public void setVaildWeek(String vaildWeek) {
		this.vaildWeek = vaildWeek;
	}


	public String getVaildTime() {
		return vaildTime;
	}


	public void setVaildTime(String vaildTime) {
		this.vaildTime = vaildTime;
	}


	public String getUnusedableDate() {
		return unusedableDate;
	}


	public void setUnusedableDate(String unusedableDate) {
		this.unusedableDate = unusedableDate;
	}


	public Date getNow() {
		return now;
	}


	public void setNow(Date now) {
		this.now = now;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getDistrictsId() {
		return districtsId;
	}


	public void setDistrictsId(String districtsId) {
		this.districtsId = districtsId;
	}


	public String getShopCategory() {
		return shopCategory;
	}


	public void setShopCategory(String shopCategory) {
		this.shopCategory = shopCategory;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getPreSell() {
		return preSell;
	}


	public void setPreSell(String preSell) {
		this.preSell = preSell;
	}


	public Date getRealPickupDate() {
		return realPickupDate;
	}


	public void setRealPickupDate(Date realPickupDate) {
		this.realPickupDate = realPickupDate;
	}


	public String getOutURL() {
		return outURL;
	}


	public void setOutURL(String outURL) {
		this.outURL = outURL;
	}


	public String getGreenMerchantId() {
		return greenMerchantId;
	}


	public void setGreenMerchantId(String greenMerchantId) {
		this.greenMerchantId = greenMerchantId;
	}


	public String getBelong() {
		return belong;
	}


	public void setBelong(String belong) {
		this.belong = belong;
	}


	public Integer getCombinationCount() {
		return combinationCount;
	}


	public void setCombinationCount(Integer combinationCount) {
		this.combinationCount = combinationCount;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getCityId() {
		return cityId;
	}


	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
   
}