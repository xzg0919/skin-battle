package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 保险订单
 */
@TableName("sb_picc_order")
@Data
public class PiccOrder  extends  DataEntity<Long> {

    private Long id ;

    /**
     * 会员Id
     */
    private Integer memberId;
    /**
     * 阿里user_id 用户唯一标识
     */
    private String aliUserId;
    /**
     * 投保单Id
     */
    private Integer insuranceId;
    /**
     * 所属公司Id
     */
    private Integer piccCompanyId;

    /**
     * 用户姓名(投保人姓名)
     */
    private String memberName;
    /**
     * 用户身份证号码
     */
    private String idCard;
    /**
     * 用户手机号码
     */
    private String memberTel;
    /**
     * 用户居住地址
     */
    private String memberAddress;
    /**
     * 保险初始额度
     */
    private Integer initPrice;
    /**
     * 保险承保额度
     */
    private Integer underwritingPrice;

    /**
     * 保险单状态
     */
    @TableField(value = "status_")
    private PiccOrderType status;

    /**
     * 审核时间
     */
    private Date auditingDate;
    /**
     * 保险单号(
     */
    private String insuranceNum;


    /**
     * 保单生效时间
     */
    private Date pickStartTime;
    /**
     * 保单失效时间
     */
    private Date pickEndTime;
    /**
     * 审核不通过的原因
     */
    private String cancelReason;

    /**
     * 创建时间
     */
    @TableField(exist = false)
    private String createTime;
    /**
     * 审核时间
     */
    @TableField(exist = false)
    private String auditingTime;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String pickStartDate;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String pickEndDate;

    public String getPickStartDate() {
        return getYmdDate(pickStartTime);
    }

    public void setPickStartDate(String pickStartDate) {
        this.pickStartDate = pickStartDate;
    }

    public String getPickEndDate() {
        return getYmdDate(pickEndTime);
    }

    public void setPickEndDate(String pickEndDate) {
        this.pickEndDate = pickEndDate;
    }

    public String getCreateTime() {
        return getDate(createDate);
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuditingTime() {
        return getYmdDate(auditingDate);
    }

    public void setAuditingTime(String auditingTime) {
        this.auditingTime = auditingTime;
    }

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

    public Integer getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Integer insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Integer getPiccCompanyId() {
        return piccCompanyId;
    }

    public void setPiccCompanyId(Integer piccCompanyId) {
        this.piccCompanyId = piccCompanyId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMemberTel() {
        return memberTel;
    }

    public void setMemberTel(String memberTel) {
        this.memberTel = memberTel;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public Integer getInitPrice() {
        return initPrice;
    }

    public void setInitPrice(Integer initPrice) {
        this.initPrice = initPrice;
    }

    public Integer getUnderwritingPrice() {
        return underwritingPrice;
    }

    public void setUnderwritingPrice(Integer underwritingPrice) {
        this.underwritingPrice = underwritingPrice;
    }

    public PiccOrderType getStatus() {
        return status;
    }

    public void setStatus(PiccOrderType status) {
        this.status = status;
    }

    public Date getAuditingDate() {
        return auditingDate;
    }

    public void setAuditingDate(Date auditingDate) {
        this.auditingDate = auditingDate;
    }

    public String getInsuranceNum() {
        return insuranceNum;
    }

    public void setInsuranceNum(String insuranceNum) {
        this.insuranceNum = insuranceNum;
    }

    public Date getPickStartTime() {
        return pickStartTime;
    }

    public void setPickStartTime(Date pickStartTime) {
        this.pickStartTime = pickStartTime;
    }

    public Date getPickEndTime() {
        return pickEndTime;
    }

    public void setPickEndTime(Date pickEndTime) {
        this.pickEndTime = pickEndTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public enum PiccOrderType implements IEnum {
        RECEIVE(0), // 已领取，审核中
        NOOPEN(1), // 未开通
        OPENING(2), // 保障中
        INVALID(3),// 已失效
        WAIT(4);// 已导出，待审核
        private int value;

        PiccOrderType(final int value) {
            this.value = value;
        }

        public Serializable getValue() {
            return this.value;
        }
    }
    public String getDate(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public String getYmdDate(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

}
