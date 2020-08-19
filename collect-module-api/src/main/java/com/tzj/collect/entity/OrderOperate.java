package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@TableName("sb_order_operate")
@Data
public class OrderOperate extends DataEntity<Long> {

    private Long id;

    private  String  orderNo; //订单号

    @TableField(value="operate_log")
    private  String operateLog;//操作日志

    private  String reason;//原因

    private  String operatorMan;//操作员

    @TableField(exist = false)
    private String createTime;


    public String getCreateTime() {
        if (this.createDate != null) {
            return this.getDate(this.createDate);
        }
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
