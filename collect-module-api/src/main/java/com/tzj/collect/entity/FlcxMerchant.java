package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * 分类商户
 */
@TableName("flcx_merchant")
@Data
public class FlcxMerchant extends DataEntity<Long> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;
    /**
     * 商户号
     */
    @TableField(value = "app_id")
    private String  appId;
    /**
     * 商户名
     */
    @TableField(value = "app_name")
    private String  appName;

    /**
     * 密钥
     */
    @TableField(value = "app_secret")
    private String appSecret;

    /**
     * 状态 0 启用 1 禁用
     */
    @TableField(value = "status_")
    private String status;



}
