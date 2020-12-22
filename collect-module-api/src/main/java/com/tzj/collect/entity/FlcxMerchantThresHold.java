package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * 分类指南  商户调用日志
 */
@TableName("flcx_merchant_thresHold")
@Data
public class FlcxMerchantThresHold extends DataEntity<Long> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;
    /**
     * 商户号
     */
    @TableField(value = "app_id")
    private String  appId;

    /**
     * 阈值 （0 表示不限）
     */
    @TableField(value = "threshold")
    private Integer Threshold;

    /**
     * 今日调用次数
     */
    @TableField(value = "today_times")
    private Integer todayTimes;





}
