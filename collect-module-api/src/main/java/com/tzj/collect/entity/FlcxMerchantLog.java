package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * 分类指南  商户调用日志
 */
@TableName("flcx_merchant_log")
@Data
public class FlcxMerchantLog extends DataEntity<Long> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;
    /**
     * 商户号
     */
    @TableField(value = "app_id")
    private String  appId;

    /**
     * 请求参数
     */
    @TableField(value = "request_param")
    private String requestParam;
    /**
     * 响应参数
     */
    @TableField(value = "response_param")
    private String responseParam;


    /**
     * 状态 （0 失败（参数错误） 1 成功并有返回 2 成功但未查到结果 4 系统繁忙）
     */
    @TableField(value = "status_")
    private Integer status;


}
