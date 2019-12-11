package com.tzj.collect.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.validator.constraints.Length;

/**
 * 
 * <p>Created on 2019年12月2日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [商品券码表]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
 */
@TableName("sb_product_code")
public class ProductCode extends DataEntity<String>
{
    /**
     * <p>Description:[]</p>
     */
    private static final long serialVersionUID = 2232536346436341L;

    /**
     *  商品类型
     */
    private String id;

    /**
     *  商品类型
     */
    private ProductType productType;

    /**
     *  优惠券类型
     */
    private VoucherType voucherType;

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
    private ValidType validType;

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
    @TableField(exist = false)
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
     * 用于查询的券类型
     */
    @TableField(exist = false)
    private VoucherType[] vTypeStr;

    /**
     * 用于查询的券类别
     */
    @TableField(exist = false)
    private ProductType[] pTypeStr;

    /**
     * 用于查询的是否上架
     */
    @TableField(exist = false)
    private String[] marketableStr;

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
    @TableField(value = "outURL")
    private String outURL;

    /**
     * 绿账商家主键
     */
    private String greenMerchantId;

    /**
     * 归属(家乐福等等)
     */
    private String belong;

    /**
    * @Fields field:field:【组合券张数】
    */
    private Integer combinationCount;

    /**
     * 商品类型0 优惠券,1 物品
     */
    private String goodsType;

    /**
     * 大图片
     */
    private String bigImg;
    /**
     * 券码
     */
    private String productCode;
    /**
     * 券状态，0未领取，1已领取
     */
    private String status;
    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 会员的阿里id
     */
    private String aliId;
    
    /**
     * 商品id
     */
    private String pId;
    
    /**
     * <p>Description:[获取商品id]</p>
     * @return Integer pId.
     */
    public String getpId()
    {
        return pId;
    }

    /**
     * <p>Description:[设置商品id]</p>
     * @param Integer pId 
     */
    public void setpId(String pId)
    {
        this.pId = pId;
    }
    
    /**
     * <p>Description:[获取券码]</p>
     * @return String productCode.
     */
    public String getProductCode()
    {
        return productCode;
    }

    /**
     * <p>Description:[设置券码]</p>
     * @param String productCode 
     */
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    /**
     * <p>Description:[获取券状态]</p>
     * @return String status.
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * <p>Description:[设置券状态]</p>
     * @param String status 
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * <p>Description:[获取会员id]</p>
     * @return Integer memberId.
     */
    public Long getMemberId()
    {
        return memberId;
    }

    /**
     * <p>Description:[设置会员id]</p>
     * @param Integer memberId 
     */
    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    /**
     * <p>Description:[获取会员的阿里id]</p>
     * @return String aliId.
     */
    public String getAliId()
    {
        return aliId;
    }

    /**
     * <p>Description:[设置会员的阿里id]</p>
     * @param String aliId 
     */
    public void setAliId(String aliId)
    {
        this.aliId = aliId;
    }

    public String getBigImg()
    {
        return bigImg;
    }

    public void setBigImg(String bigImg)
    {
        this.bigImg = bigImg;
    }

    public String getGoodsType()
    {
        return goodsType;
    }

    public void setGoodsType(String goodsType)
    {
        this.goodsType = goodsType;
    }

    /**
     * <p>Description:[获取归属]</p>
     * @return String belong.
     */
    public String getBelong()
    {
        return belong;
    }

    /**
     * <p>Description:[设置归属]</p>
     * @param String belong 
     */
    public void setBelong(String belong)
    {
        this.belong = belong;
    }

    /**
     * <p>Description:[获取预售商品1:是]</p>
     * @return String preSell.
     */
    public String getPreSell()
    {
        return preSell;
    }

    /**
     * <p>Description:[设置预售商品1:是]</p>
     * @param String preSell 
     */
    public void setPreSell(String preSell)
    {
        this.preSell = preSell;
    }

    /**
     * <p>Description:[获取实际开始领取日期]</p>
     * @return Date realPickupDate.
     */
    public Date getRealPickupDate()
    {
        return realPickupDate;
    }

    /**
     * <p>Description:[设置实际开始领取日期]</p>
     * @param Date realPickupDate 
     */
    public void setRealPickupDate(Date realPickupDate)
    {
        this.realPickupDate = realPickupDate;
    }

    /**
     * <p>Description:[获取门店地址]</p>
     * @return String address.
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * <p>Description:[设置门店地址]</p>
     * @param String address 
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * <p>Discription:[构造器方法描述]</p>
     * @coustructor 方法.
     */
    public ProductCode()
    {
        super();
    }

    /**
     * <p>Description:[获取区id]</p>
     * @return String districtsId.
     */
    public String getDistrictsId()
    {
        return districtsId;
    }

    /**
     * <p>Description:[设置区id]</p>
     * @param districtsId 
     */
    public void setDistrictsId(final String districtsId)
    {
        this.districtsId = districtsId;
    }

    /**
     * <p>Description:[获取行业分类]</p>
     * @return String shopCategory.
     */
    public String getShopCategory()
    {
        return shopCategory;
    }

    /**
     * <p>Description:[设置行业分类]</p>
     * @param shopCategory 
     */
    public void setShopCategory(final String shopCategory)
    {
        this.shopCategory = shopCategory;
    }

    /**
     * <p>Discription:[获取品牌]</p>
     * @return String brand.
     */
    public String getBrand()
    {
        return brand;
    }

    /**
     * <p>Discription:[设置品牌]</p>
     * @param brand 
     */
    public void setBrand(final String brand)
    {
        this.brand = brand;
    }

    /**
     * <p>Discription:[获取电话]</p>
     * @return String phone.
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * <p>Discription:[设置电话]</p>
     * @param phone 
     */
    public void setPhone(final String phone)
    {
        this.phone = phone;
    }

    /**
     * <p>Discription:[获取库存]</p>
     * @return Integer stock.
     */
    public Integer getStock()
    {
        return stock;
    }

    /**
     * <p>Discription:[设置库存]</p>
     * @param stock 
     */
    public void setStock(final Integer stock)
    {
        this.stock = stock;
    }

    /**
     * <p>Discription:[获取是否上架]</p>
     * @return String isMarketable.
     */
    public String getIsMarketable()
    {
        return isMarketable;
    }

    /**
     * <p>Discription:[设置是否上架]</p>
     * @param isMarketable 
     */
    public void setIsMarketable(final String isMarketable)
    {
        this.isMarketable = isMarketable;
    }

    @Length(min = 0, max = 50, message = "商品类型长度必须介于 0 和 50 之间")
    /**
     * <p>Discription:[获取商品类型]</p>
     * @return String productType
     */
    public ProductType getProductType()
    {
        return productType;
    }

    /**
     * <p>Discription:[设置商品类型]</p>
     * @param productType
     */
    public void setProductType(final ProductType productType)
    {
        this.productType = productType;
    }

    @Length(min = 0, max = 50, message = "优惠券类型长度必须介于 0 和 50 之间")
    /**
     * <p>Discription:[获取优惠券类型]</p>
     * @return String voucherType
     */
    public VoucherType getVoucherType()
    {
        return voucherType;
    }

    /**
     * <p>Discription:[设置优惠券类型]</p>
     * @param voucherType
     */
    public void setVoucherType(final VoucherType voucherType)
    {
        this.voucherType = voucherType;
    }

    /**
     * <p>Discription:[获取兑换所需积分]</p>
     * @return Integer bindingPoint
     */
    public Integer getBindingPoint()
    {
        return bindingPoint;
    }

    /**
     * <p>Discription:[设置兑换所需积分]</p>
     * @param bindingPoint
     */
    public void setBindingPoint(final Integer bindingPoint)
    {
        this.bindingPoint = bindingPoint;
    }

    /**
     * <p>Discription:[获取兑换所需金额]</p>
     * @return BigDecimal bindingPrice
     */
    public BigDecimal getBindingPrice()
    {
        return bindingPrice;
    }

    /**
     * <p>Discription:[设置兑换所需金额]</p>
     * @param bindingPrice
     */
    public void setBindingPrice(final BigDecimal bindingPrice)
    {
        this.bindingPrice = bindingPrice;
    }

    /**
     * <p>Discription:[获取优惠券面额]</p>
     * @return BigDecimal amount
     */
    public BigDecimal getAmount()
    {
        return amount;
    }

    /**
     * <p>Discription:[设置优惠券面额]</p>
     * @param amount
     */
    public void setAmount(final BigDecimal amount)
    {
        this.amount = amount;
    }

    @Length(min = 0, max = 100, message = "商品图片长度必须介于 0 和 50 之间")
    /**
     * <p>Discription:[获取商品图片]</p>
     * @return String img
     */
    public String getImg()
    {
        return img;
    }

    /**
     * <p>Discription:[设置商品图片]</p>
     * @param img
     */
    public void setImg(final String img)
    {
        this.img = img;
    }

    /**
     * <p>Discription:[获取最低消费金额]</p>
     * @return BigDecimal lowLimit
     */
    public BigDecimal getLowLimit()
    {
        return lowLimit;
    }

    /**
     * <p>Discription:[设置最低消费金额]</p>
     * @param lowLimit
     */
    public void setLowLimit(final BigDecimal lowLimit)
    {
        this.lowLimit = lowLimit;
    }

    @Length(min = 0, max = 50, message = "商品描述长度必须介于 0 和 50 之间")
    /**
     * <p>Discription:[获取商品描述]</p>
     * @return String content
     */
    public String getContent()
    {
        return content;
    }

    /**
     * <p>Discription:[设置商品描述]</p>
     * @param content
     */
    public void setContent(final String content)
    {
        this.content = content;
    }

    @Length(min = 0, max = 50, message = "商品编号长度必须介于 0 和 50 之间")
    /**
     * <p>Discription:[获取商品编号]</p>
     * @return String sn
     */
    public String getSn()
    {
        return sn;
    }

    /**
     * <p>Discription:[设置商品编号]</p>
     * @param sn
     */
    public void setSn(final String sn)
    {
        this.sn = sn;
    }

    /**
     * <p>Discription:[获取已兑换数量]</p>
     * @return Integer bindingQuantity
     */
    public Integer getBindingQuantity()
    {
        return bindingQuantity;
    }

    /**
     * <p>Discription:[设置已兑换数量]</p>
     * @param bindingQuantity
     */
    public void setBindingQuantity(final Integer bindingQuantity)
    {
        this.bindingQuantity = bindingQuantity;
    }

    @Length(min = 0, max = 255, message = "商品名称长度必须介于 0 和 255 之间")
    /**
     * <p>Discription:[获取商品名称]</p>
     * @return String name
     */
    public String getName()
    {
        return name;
    }

    /**
     * <p>Discription:[设置商品名称]</p>
     * @param name
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    @Length(min = 0, max = 255, message = "商品副名称长度必须介于 0 和 255 之间")
    /**
     * <p>Discription:[获取商品副名称]</p>
     * @return String subName
     */
    public String getSubName()
    {
        return subName;
    }

    /**
     * <p>Discription:[设置商品副名称]</p>
     * @param subName
     */
    public void setSubName(final String subName)
    {
        this.subName = subName;
    }

    /**
     * <p>Discription:[获取商品原价]</p>
     * @return BigDecimal oldPrice
     */
    public BigDecimal getOldPrice()
    {
        return oldPrice;
    }

    /**
     * <p>Discription:[设置商品原价]</p>
     * @param oldPrice
     */
    public void setOldPrice(final BigDecimal oldPrice)
    {
        this.oldPrice = oldPrice;
    }

    /**
     * <p>Discription:[获取]</p>
     * @return String vTypeStr.
     */
    public VoucherType[] getvTypeStr()
    {
        return vTypeStr;
    }

    /**
     * <p>Discription:[设置]</p>
     * @param vTypeStr 
     */
    public void setvTypeStr(final VoucherType[] vTypeStr)
    {
        this.vTypeStr = vTypeStr;
    }

    /**
     * <p>Discription:[获取]</p>
     * @return String pTypeStr.
     */
    public ProductType[] getpTypeStr()
    {
        return pTypeStr;
    }

    /**
     * <p>Discription:[设置]</p>
     * @param pTypeStr 
     */
    public void setpTypeStr(final ProductType[] pTypeStr)
    {
        this.pTypeStr = pTypeStr;
    }

    /**
     * <p>Discription:[获取]</p>
     * @return String marketableStr.
     */
    public String[] getMarketableStr()
    {
        return marketableStr;
    }

    /**
     * <p>Discription:[设置]</p>
     * @param marketableStr 
     */
    public void setMarketableStr(final String[] marketableStr)
    {
        this.marketableStr = marketableStr;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    /**
     * <p>Discription:[获取领取开始日期]</p>
     * @return Date pickStartDate
     */
    public Object getPickStartDate()
    {

        return new SimpleDateFormat("yyyy-MM-dd").format(pickStartDate) + " 00:00:01";
    }

    /**
     * <p>Discription:[设置领取开始日期]</p>
     * @param pickStartDate
     */
    public void setPickStartDate(final Date pickStartDate)
    {
        this.pickStartDate = pickStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    /**
     * <p>Discription:[获取领取结束日期]</p>
     * @return Date pickEndDate
     */
    public Object getPickEndDate()
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(pickEndDate) + " 23:59:59";
    }

    /**
     * <p>Discription:[设置领取结束日期]</p>
     * @param pickEndDate
     */
    public void setPickEndDate(final Date pickEndDate)
    {
        this.pickEndDate = pickEndDate;
    }

    @Length(min = 0, max = 50, message = "有效日期类型长度必须介于 0 和 50 之间")
    /**
     * <p>Discription:[获取有效日期类型]</p>
     * @return String validType
     */
    public ValidType getValidType()
    {
        return validType;
    }

    /**
     * <p>Discription:[设置有效日期类型]</p>
     * @param validType
     */
    public void setValidType(final ValidType validType)
    {
        this.validType = validType;
    }

    @Length(min = 0, max = 50, message = "领取后第n天长度必须介于 0 和 50 之间")
    /**
     * <p>Discription:[获取领取后第n天]</p>
     * @return String validFromDay
     */
    public String getValidFromDay()
    {
        return validFromDay;
    }

    /**
     * <p>Discription:[设置领取后第n天]</p>
     * @param validFromDay
     */
    public void setValidFromDay(final String validFromDay)
    {
        this.validFromDay = validFromDay;
    }

    @Length(min = 0, max = 50, message = "领取后第m天长度必须介于 0 和 50 之间")
    /**
     * <p>Discription:[获取领取后第m天]</p>
     * @return String validToDay
     */
    public String getValidToDay()
    {
        return validToDay;
    }

    /**
     * <p>Discription:[设置领取后第m天]</p>
     * @param validToDay
     */
    public void setValidToDay(final String validToDay)
    {
        this.validToDay = validToDay;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    /**
     * <p>Discription:[获取有效期开始日期]</p>
     * @return Date validStartDate
     */
    public Object getValidStartDate()
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(validStartDate);
    }

    /**
     * <p>Discription:[设置有效期开始日期]</p>
     * @param validStartDate
     */
    public void setValidStartDate(final Date validStartDate)
    {
        this.validStartDate = validStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    /**
     * <p>Discription:[获取有效期结束日期]</p>
     * @return Date validEndDate
     */
    public Object getValidEndDate()
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(validEndDate);
    }

    /**
     * <p>Discription:[设置有效期结束日期]</p>
     * @param validEndDate
     */
    public void setValidEndDate(final Date validEndDate)
    {
        this.validEndDate = validEndDate;
    }

    @Length(min = 0, max = 10, message = "领取限制周期：天周月长度必须介于 0 和 10 之间")
    /**
     * <p>Discription:[获取领取限制周期：天周月]</p>
     * @return String pickLimitPeriod
     */
    public String getPickLimitPeriod()
    {
        return pickLimitPeriod;
    }

    /**
     * <p>Discription:[设置领取限制周期：天周月]</p>
     * @param pickLimitPeriod
     */
    public void setPickLimitPeriod(final String pickLimitPeriod)
    {
        this.pickLimitPeriod = pickLimitPeriod;
    }

    @Length(min = 0, max = 6, message = "领取限制周期内最多领取数量长度必须介于 0 和 6 之间")
    /**
     * <p>Discription:[获取领取限制周期内最多领取数量]</p>
     * @return String pickLimitNum
     */
    public String getPickLimitNum()
    {
        return pickLimitNum;
    }

    /**
     * <p>Discription:[设置领取限制周期内最多领取数量]</p>
     * @param pickLimitNum
     */
    public void setPickLimitNum(final String pickLimitNum)
    {
        this.pickLimitNum = pickLimitNum;
    }

    @Length(min = 0, max = 6, message = "最多领取数量长度必须介于 0 和 6 之间")
    /**
     * <p>Discription:[获取最多领取数量]</p>
     * @return String pickLimitTotal
     */
    public String getPickLimitTotal()
    {
        return pickLimitTotal;
    }

    /**
     * <p>Discription:[设置最多领取数量]</p>
     * @param pickLimitTotal
     */
    public void setPickLimitTotal(final String pickLimitTotal)
    {
        this.pickLimitTotal = pickLimitTotal;
    }

    @Length(min = 0, max = 100, message = "周几可用长度必须介于 0 和 100 之间")
    /**
     * <p>Discription:[获取周几可用]</p>
     * @return String vaildWeek
     */
    public String getVaildWeek()
    {
        return vaildWeek;
    }

    /**
     * <p>Discription:[设置周几可用]</p>
     * @param vaildWeek
     */
    public void setVaildWeek(final String vaildWeek)
    {
        this.vaildWeek = vaildWeek;
    }

    @Length(min = 0, max = 200, message = "可用时间长度必须介于 0 和 200 之间")
    /**
     * <p>Discription:[获取可用时间]</p>
     * @return String vaildTime
     */
    public String getVaildTime()
    {
        return vaildTime;
    }

    /**
     * <p>Discription:[设置可用时间]</p>
     * @param vaildTime
     */
    public void setVaildTime(final String vaildTime)
    {
        this.vaildTime = vaildTime;
    }

    @Length(min = 0, max = 200, message = "不可使用日期长度必须介于 0 和 200 之间")
    /**
     * <p>Discription:[获取不可使用日期]</p>
     * @return String unusedableDate
     */
    public String getUnusedableDate()
    {
        return unusedableDate;
    }

    /**
     * <p>Discription:[设置不可使用日期]</p>
     * @param unusedableDate
     */
    public void setUnusedableDate(final String unusedableDate)
    {
        this.unusedableDate = unusedableDate;
    }

    /**
     * <p>Discription:[获取用于查询时间段]</p>
     * @return Date now.
     */
    public Date getNow()
    {
        return now;
    }

    /**
     * <p>Discription:[设置用于查询时间段]</p>
     * @param now 
     */
    public void setNow(final Date now)
    {
        this.now = now;
    }

    public String getOutURL()
    {
        return outURL;
    }

    public void setOutURL(String outURL)
    {
        this.outURL = outURL;
    }

    public String getGreenMerchantId()
    {
        return greenMerchantId;
    }

    public void setGreenMerchantId(String greenMerchantId)
    {
        this.greenMerchantId = greenMerchantId;
    }

    //	/**
    //     *  Created on 2017年3月26日 
    //     * <p>Discription:[用于页面展示的券结束日期]</p>
    //     * @author:[杨欢][yanghuan1937@aliyun.com] 
    //     * @update:[日期YYYY-MM-DD] [更改人姓名]
    //     * @return String
    //     */
    //    @Transient
    //    @JsonProperty
    //    public String getPageEndDate()
    //    {
    //        String pageEndDate = null;
    //        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
    //        if (ValidType.absolute.equals(this.validType))
    //        {
    //            pageEndDate = formatter.format(this.validEndDate);
    //        }
    //        else
    //        {
    //            Calendar now = Calendar.getInstance();
    //            now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + Integer.valueOf(this.validToDay));
    //            pageEndDate = formatter.format(now.getTime());
    //        }
    //        return pageEndDate;
    //    }
    //
    //    /**
    //     *  Created on 2017年3月26日 
    //     * <p>Discription:[用于页面展示的券开始日期]</p>
    //     * @author:[杨欢][yanghuan1937@aliyun.com] 
    //     * @update:[日期YYYY-MM-DD] [更改人姓名]
    //     * @return String
    //     */
    //    @Transient
    //    @JsonProperty
    //    public String getPageStartDate()
    //    {
    //        String pageEndDate = null;
    //        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
    //        if (ValidType.absolute.equals(this.validType))
    //        {
    //            pageEndDate = formatter.format(this.validStartDate);
    //        }
    //        return pageEndDate;
    //    }
    //
    //    /**
    //     *  Created on 2017年3月26日 
    //     * <p>Discription:[页面的温馨提示]</p>
    //     * @author:[杨欢][yanghuan1937@aliyun.com] 
    //     * @update:[日期YYYY-MM-DD] [更改人姓名]
    //     * @return String
    //     */
    //    @Transient
    //    @JsonProperty
    //    public String getPageContent()
    //    {
    //        StringBuffer pageContent = new StringBuffer();
    //        String[] tmpContentp = null;
    //        if (!StringUtils.isBlank(this.content))
    //        {
    //            tmpContentp = this.content.split("\r\n");
    //            for (int i = 0, j = tmpContentp.length; i < j; i++)
    //            {
    //                pageContent.append("<li>");
    //                pageContent.append(tmpContentp[i]);
    //                pageContent.append("</li>");
    //            }
    //        }
    //        return pageContent.toString();
    //    }

    public Integer getCombinationCount()
    {
        return combinationCount;
    }

    public void setCombinationCount(Integer combinationCount)
    {
        this.combinationCount = combinationCount;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

}