package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 *
 *黑名单 表
 *
 *
 **/
@TableName("sb_black_list")
@Data
public class BlackList extends  DataEntity<Long>{
    private static final long serialVersionUID = -6405467088491154588L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 阿里userid
     */
    private  String aliUserId;

    // 限制下单类型 TitleType(0,1,2,3,4,5....)
    private String limitType;
}
