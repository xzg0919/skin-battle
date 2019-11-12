package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

/**
 * 保险订单所导出的数据
 */
@TableName("sb_picc_num")
public class PiccNum extends  DataEntity<Long> {

    private Long id ;

    /**
     * 投保公司Id
     */
    private Integer piccCompanyId;

    /**
     * 导出数量
     */
    private Integer outNum;

    /**
     * 导出时间
     */
    private Date outTime;

    /**
     * 导出成功数量（审核通过数量）
     */
    private Integer inSuccessNum;

    /**
     * 导出成功时间（审核通过时间）
     */
    private Date inSuccessTime;

    /**
     * 导出失败数量（审核失败数量）
     */
    private Integer inErrorNum;

    /**
     * 导出失败时间（审核失败时间）
     */
    private Date inErrorTime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPiccCompanyId() {
        return piccCompanyId;
    }

    public void setPiccCompanyId(Integer piccCompanyId) {
        this.piccCompanyId = piccCompanyId;
    }

    public Integer getOutNum() {
        return outNum;
    }

    public void setOutNum(Integer outNum) {
        this.outNum = outNum;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public Integer getInSuccessNum() {
        return inSuccessNum;
    }

    public void setInSuccessNum(Integer inSuccessNum) {
        this.inSuccessNum = inSuccessNum;
    }

    public Date getInSuccessTime() {
        return inSuccessTime;
    }

    public void setInSuccessTime(Date inSuccessTime) {
        this.inSuccessTime = inSuccessTime;
    }

    public Integer getInErrorNum() {
        return inErrorNum;
    }

    public void setInErrorNum(Integer inErrorNum) {
        this.inErrorNum = inErrorNum;
    }

    public Date getInErrorTime() {
        return inErrorTime;
    }

    public void setInErrorTime(Date inErrorTime) {
        this.inErrorTime = inErrorTime;
    }
}
