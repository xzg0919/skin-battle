package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020-03-19 10:45
 * @Description:
 */
@Data
@TableName(value = "wx_paymeny_error")
public class WxPaymentError extends DataEntity<Long> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String openId;
    private String orderNo;
    private int totalFee;
    private String status;
    private String errorMsg;
}