package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [券码实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
@TableName("sb_voucher_code")
public class VoucherCode extends DataEntity<Long>
{
	private static final long serialVersionUID = -5430971499748254322L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String voucherName;
    /**
     * 类型
     */
    private String voucherType;//A 代金券  B 折扣券  C 满减券
    /**
     * 折扣
     */
    private java.math.BigDecimal dis;
    /**
     * 适用订单类型
     */
    private String orderType;
    /**
     * 面额
     */
    private Integer money;
    /**
     * 最低使用金额
     */
    private Integer lowMoney;
    /**
     * 最高抵扣金额
     */
    private Integer topMoney;
    /**
     * 领取开始日期
     */
    private java.util.Date pickupStart;
    /**
     * 领取结束日期
     */
    private java.util.Date pickupEnd;
    /**
     * 有效期类型
     */
    private String validType;
    /**
     * 领取后n天有效
     */
    private Integer validDay;
    /**
     * 可用开始日期
     */
    private java.util.Date validStart;
    /**
     * 可用结束日期
     */
    private java.util.Date validEnd;
    /**
     * 券数量
     */
    private Long voucherCount;
    /**
     * 领取限制
     */
    private Integer pickLimitTotal;
    /**
     * 券码
     */
    private String voucherCode;
    /**
     * 券id
     */
    private Long voucherId;

    /**
     * 会员id
     */
    private Long memberId;
    
    /**
     * <p>Description:[获取]</p>
     * @return Long memberId.
     */
    public Long getMemberId()
    {
        return memberId;
    }
    /**
     * <p>Description:[设置]</p>
     * @param Long memberId 
     */
    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }
    /**
     * <p>Description:[获取id]</p>
     * @return String id
     */
    @Override
    public Long getId()
    {
        return id;
    }    /**
     * <p>Description:[设置id]</p>
     * @param String id 
     */
    @Override
    public void setId(Long id)
    {
        this.id=id;
    }
    /**
     * <p>Description:[获取适用订单类型]</p>
     * @return String orderType
     */
    public String getOrderType()
    {
        return orderType;
    }
    /**
     * <p>Description:[设置适用订单类型]</p>
     * @param String orderType
     */

    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }
    /**
     * <p>Description:[获取名称]</p>
     * @return String voucherName
     */
    public String getVoucherName()
    {
        return voucherName;
    }
    /**
     * <p>Description:[设置名称]</p>
     * @param String voucherName
     */

    public void setVoucherName(String voucherName)
    {
        this.voucherName = voucherName;
    }

    /**
     * <p>Description:[获取类型]</p>
     * @return String voucherType
     */
    public String getVoucherType()
    {
        return voucherType;
    }
    /**
     * <p>Description:[设置类型]</p>
     * @param String voucherType
     */

    public void setVoucherType(String voucherType)
    {
        this.voucherType = voucherType;
    }

    /**
     * <p>Description:[获取折扣]</p>
     * @return java.math.BigDecimal dis
     */
    public java.math.BigDecimal getDis()
    {
        return dis;
    }
    /**
     * <p>Description:[设置折扣]</p>
     * @param java.math.BigDecimal dis
     */

    public void setDis(java.math.BigDecimal dis)
    {
        this.dis = dis;
    }

    /**
     * <p>Description:[获取面额]</p>
     * @return Integer money
     */
    public Integer getMoney()
    {
        return money;
    }
    /**
     * <p>Description:[设置面额]</p>
     * @param Integer money
     */

    public void setMoney(Integer money)
    {
        this.money = money;
    }

    /**
     * <p>Description:[获取最低使用金额]</p>
     * @return Integer lowMoney
     */
    public Integer getLowMoney()
    {
        return lowMoney;
    }
    /**
     * <p>Description:[设置最低使用金额]</p>
     * @param Integer lowMoney
     */

    public void setLowMoney(Integer lowMoney)
    {
        this.lowMoney = lowMoney;
    }

    /**
     * <p>Description:[获取最高抵扣金额]</p>
     * @return Integer topMoney
     */
    public Integer getTopMoney()
    {
        return topMoney;
    }
    /**
     * <p>Description:[设置最高抵扣金额]</p>
     * @param Integer topMoney
     */

    public void setTopMoney(Integer topMoney)
    {
        this.topMoney = topMoney;
    }

    /**
     * <p>Description:[获取领取开始日期]</p>
     * @return String pickupStart
     */
    public java.util.Date getPickupStart()
    {
        return pickupStart;
    }
    /**
     * <p>Description:[设置领取开始日期]</p>
     * @param String pickupStart
     */

    public void setPickupStart(java.util.Date pickupStart)
    {
        this.pickupStart = pickupStart;
    }

    /**
     * <p>Description:[获取领取结束日期]</p>
     * @return java.util.Date pickupEnd
     */
    public java.util.Date getPickupEnd()
    {
        return pickupEnd;
    }
    /**
     * <p>Description:[设置领取结束日期]</p>
     * @param java.util.Date pickupEnd
     */

    public void setPickupEnd(java.util.Date pickupEnd)
    {
        this.pickupEnd = pickupEnd;
    }

    /**
     * <p>Description:[获取有效期类型]</p>
     * @return java.util.Date validType
     */
    public String getValidType()
    {
        return validType;
    }
    /**
     * <p>Description:[设置有效期类型]</p>
     * @param java.util.Date validType
     */

    public void setValidType(String validType)
    {
        this.validType = validType;
    }

    /**
     * <p>Description:[获取领取后n天有效]</p>
     * @return Integer validDay
     */
    public Integer getValidDay()
    {
        return validDay;
    }
    /**
     * <p>Description:[设置领取后n天有效]</p>
     * @param Integer validDay
     */

    public void setValidDay(Integer validDay)
    {
        this.validDay = validDay;
    }

    /**
     * <p>Description:[获取可用开始日期]</p>
     * @return java.util.Date validStart
     */
    public java.util.Date getValidStart()
    {
        return validStart;
    }
    /**
     * <p>Description:[设置可用开始日期]</p>
     * @param java.util.Date validStart
     */

    public void setValidStart(java.util.Date validStart)
    {
        this.validStart = validStart;
    }

    /**
     * <p>Description:[获取可用结束日期]</p>
     * @return java.util.Date validEnd
     */
    public java.util.Date getValidEnd()
    {
        return validEnd;
    }
    /**
     * <p>Description:[设置可用结束日期]</p>
     * @param java.util.Date validEnd
     */

    public void setValidEnd(java.util.Date validEnd)
    {
        this.validEnd = validEnd;
    }

    /**
     * <p>Description:[获取券数量]</p>
     * @return Long voucherCount
     */
    public Long getVoucherCount()
    {
        return voucherCount;
    }
    /**
     * <p>Description:[设置券数量]</p>
     * @param Long voucherCount
     */

    public void setVoucherCount(Long voucherCount)
    {
        this.voucherCount = voucherCount;
    }

    /**
     * <p>Description:[获取领取限制]</p>
     * @return Integer pickLimitTotal
     */
    public Integer getPickLimitTotal()
    {
        return pickLimitTotal;
    }
    /**
     * <p>Description:[设置领取限制]</p>
     * @param Integer pickLimitTotal
     */

    public void setPickLimitTotal(Integer pickLimitTotal)
    {
        this.pickLimitTotal = pickLimitTotal;
    }

    /**
     * <p>Description:[获取券码]</p>
     * @return String voucherCode
     */
    public String getVoucherCode()
    {
        return voucherCode;
    }
    /**
     * <p>Description:[设置券码]</p>
     * @param String voucherCode
     */

    public void setVoucherCode(String voucherCode)
    {
        this.voucherCode = voucherCode;
    }

    /**
     * <p>Description:[获取券id]</p>
     * @return Long voucherId
     */
    public Long getVoucherId()
    {
        return voucherId;
    }
    /**
     * <p>Description:[设置券id]</p>
     * @param Long voucherId
     */

    public void setVoucherId(Long voucherId)
    {
        this.voucherId = voucherId;
    }
}
