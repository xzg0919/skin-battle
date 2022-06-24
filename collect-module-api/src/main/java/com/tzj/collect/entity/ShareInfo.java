package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@TableName("sb_share_info")
public class ShareInfo extends DataEntity<Long> {
	private Long id;

	String aliUserId;

    String shareAliUserId;

    BigDecimal bonus;

    @TableField(value="name_")
    String name;

    String tel;


}
