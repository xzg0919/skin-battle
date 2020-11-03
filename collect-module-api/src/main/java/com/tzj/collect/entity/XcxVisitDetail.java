package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;


/**
 * 通过外部渠道访问小程序明细
 */
@TableName("sb_xcx_visit_detail")
@Data
public class XcxVisitDetail extends DataEntity<Long> {
    /**
     * id
     */
    Long id;

    String code;

    @TableField(value = "name_")
    String name;


    String aliUserId;


}
