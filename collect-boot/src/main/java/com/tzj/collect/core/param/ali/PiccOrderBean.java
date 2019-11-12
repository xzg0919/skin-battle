package com.tzj.collect.core.param.ali;

import com.tzj.collect.entity.PiccOrder;

import java.util.Date;

public class PiccOrderBean {
    private String id ;

    private String ids ;
    /**
     * 会员Id
     */
    private Integer memberId;
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
    private PiccOrder.PiccOrderType status;

    /**
     * 审核时间
     */
    private Date auditingDate;
    /**
     * 保单号
     */
    private Integer insuranceNum;


    private PageBean pageBean;

    private String startTime;

    private String endTime;

    private String auditingStartTime;

    private String auditingEndTime;


    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public PiccOrder.PiccOrderType getStatus() {
        return status;
    }

    public void setStatus(PiccOrder.PiccOrderType status) {
        this.status = status;
    }

    public Date getAuditingDate() {
        return auditingDate;
    }

    public void setAuditingDate(Date auditingDate) {
        this.auditingDate = auditingDate;
    }

    public Integer getInsuranceNum() {
        return insuranceNum;
    }

    public void setInsuranceNum(Integer insuranceNum) {
        this.insuranceNum = insuranceNum;
    }

    public String getAuditingStartTime() {
        return auditingStartTime;
    }

    public void setAuditingStartTime(String auditingStartTime) {
        this.auditingStartTime = auditingStartTime;
    }

    public String getAuditingEndTime() {
        return auditingEndTime;
    }

    public void setAuditingEndTime(String auditingEndTime) {
        this.auditingEndTime = auditingEndTime;
    }
}
