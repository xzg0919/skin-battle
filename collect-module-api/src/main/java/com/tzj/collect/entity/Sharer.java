package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@TableName("sb_sharer")
public class Sharer extends DataEntity<Long> {
	private Long id;

	String aliUserId;

	/** 最后一次分享时间 **/
	Date lastShareTime;

	/** 成功分享次数 **/
	Integer successShareNum;

	/** 总分享次数 **/
	Integer totalShareNum;


	/** 总奖金 **/
	BigDecimal totalBonus;


	@TableField("status_")
	Integer status;


	String qrCode;

}
