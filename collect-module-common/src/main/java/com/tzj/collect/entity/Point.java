package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_point")
@Data
public class Point extends DataEntity<Long> {
	private Long id;
	private Integer memberId;  //会员id
	/**
	 * 阿里user_id 用户唯一标识
	 */
	private String aliUserId;
	@TableField(exist=false)
	private Member member;    
	private double point;	//积分
	private double remainPoint;//用户剩余的积分



}
