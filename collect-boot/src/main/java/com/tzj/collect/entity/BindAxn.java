package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_bind_axn")
@Data
public class BindAxn  extends DataEntity<Long> {

    private Long id;

    @TableField(value = "status_")
    private String status;//状态 0绑定中  1解绑',

    private String subsId;//'订购关系ID',

    private String secretNo;//'隐私号码，即X号码',

    private String phoneNoA;//'AXN中的A号码 及被隐藏的号码',

    private String phoneNoB;//'用户A拨打X号码时会转接到回收人员B号码',


}
