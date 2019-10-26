package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [优惠券通知实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
@TableName("sb_voucher_nofity")
public class VoucherNofity extends DataEntity<Long>
{
	private static final long serialVersionUID = -5430971499748254323L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 通知类型
     */
    private String nofityType;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 业务单号
     */
    private String alipayBizNo;
    /**
     * 发放时间
     */
    private String bizTime;
    /**
     * 券码
     */
    private String entityNum;
    /**
     * 数量
     */
    private String fluxAmount;
    /**
     * 券模板id
     */
    private String templateId;
    /**
     * 券id
     */
    private String voucherId;
    /**
     * 通知状态
     */
    private String notifyStatus;
    /**
     * 处理情况
     */
    private String notifyRemark;
    
    /**
     * <p>Description:[获取通知状态]</p>
     * @return String notifyStatus.
     */
    public String getNotifyStatus()
    {
        return notifyStatus;
    }
    /**
     * <p>Description:[设置通知状态]</p>
     * @param String notifyStatus 
     */
    public void setNotifyStatus(String notifyStatus)
    {
        this.notifyStatus = notifyStatus;
    }
    /**
     * <p>Description:[获取处理情况]</p>
     * @return String notifyRemark.
     */
    public String getNotifyRemark()
    {
        return notifyRemark;
    }
    /**
     * <p>Description:[设置处理情况]</p>
     * @param String notifyRemark 
     */
    public void setNotifyRemark(String notifyRemark)
    {
        this.notifyRemark = notifyRemark;
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
     * <p>Description:[获取通知类型]</p>
     * @return String nofityType
     */
    public String getNofityType()
    {
        return nofityType;
    }
    /**
     * <p>Description:[设置通知类型]</p>
     * @param String nofityType
     */

    public void setNofityType(String nofityType)
    {
        this.nofityType = nofityType;
    }

    /**
     * <p>Description:[获取用户id]</p>
     * @return String uid
     */
    public String getUid()
    {
        return uid;
    }
    /**
     * <p>Description:[设置用户id]</p>
     * @param String uid
     */

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    /**
     * <p>Description:[获取业务单号]</p>
     * @return String alipayBizNo
     */
    public String getAlipayBizNo()
    {
        return alipayBizNo;
    }
    /**
     * <p>Description:[设置业务单号]</p>
     * @param String alipayBizNo
     */

    public void setAlipayBizNo(String alipayBizNo)
    {
        this.alipayBizNo = alipayBizNo;
    }

    /**
     * <p>Description:[获取发放时间]</p>
     * @return String bizTime
     */
    public String getBizTime()
    {
        return bizTime;
    }
    /**
     * <p>Description:[设置发放时间]</p>
     * @param String bizTime
     */

    public void setBizTime(String bizTime)
    {
        this.bizTime = bizTime;
    }

    /**
     * <p>Description:[获取券码]</p>
     * @return String entityNum
     */
    public String getEntityNum()
    {
        return entityNum;
    }
    /**
     * <p>Description:[设置券码]</p>
     * @param String entityNum
     */

    public void setEntityNum(String entityNum)
    {
        this.entityNum = entityNum;
    }

    /**
     * <p>Description:[获取数量]</p>
     * @return String fluxAmount
     */
    public String getFluxAmount()
    {
        return fluxAmount;
    }
    /**
     * <p>Description:[设置数量]</p>
     * @param String fluxAmount
     */

    public void setFluxAmount(String fluxAmount)
    {
        this.fluxAmount = fluxAmount;
    }

    /**
     * <p>Description:[获取券模板id]</p>
     * @return String templateId
     */
    public String getTemplateId()
    {
        return templateId;
    }
    /**
     * <p>Description:[设置券模板id]</p>
     * @param String templateId
     */

    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }

    /**
     * <p>Description:[获取券id]</p>
     * @return String voucherId
     */
    public String getVoucherId()
    {
        return voucherId;
    }
    /**
     * <p>Description:[设置券id]</p>
     * @param String voucherId
     */

    public void setVoucherId(String voucherId)
    {
        this.voucherId = voucherId;
    }
}
