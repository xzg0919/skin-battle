package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 *
 *短信黑名单 表
 *
 *
 **/
@TableName("sb_sms_black_list")
@Data
public class SmsBlackList extends  DataEntity<Long>{
    private static final long serialVersionUID = -6405467088491154588L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号
     */
    private  String tel;

}
