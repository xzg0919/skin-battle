package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 保险订单
 */
@TableName("sb_picc_water")
@Data
public class PiccWater extends  DataEntity<Long> {

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
     * 是否领取 0未领取
     */
    @TableField(value = "status_")
    private String status;

    /**
     * 能量
     */
    private Integer pointCount;

    /**
     * 备注
     */
    private String descrb;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPointCount() {
        return pointCount;
    }

    public void setPointCount(Integer pointCount) {
        this.pointCount = pointCount;
    }

    public String getDescrb() {
        return descrb;
    }

    public void setDescrb(String descrb) {
        this.descrb = descrb;
    }
}
