package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 *取消申请表
 *
 * @Author 王灿
 **/
@TableName("sb_order_cancle_examine")
@Data
public class OrderCancleExamine extends  DataEntity<Long>{

    private Long id;
    //'订单号'
    private String orderNo;
    //状态 0待处理  1同意取消  2拒绝
    @TableField(value = "status_")
    private String status;
    //申请取消原因
    private String cancleReason;


}
