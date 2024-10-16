package com.skin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("point_info")
@Data
public class PointInfo extends DataEntity{

	/** userid */
	private Long userId ;

	/** 总积分 */
	private BigDecimal totalPoint ;
	/** 消纳积分 */
	private BigDecimal consumePoint ;
	/** 剩余积分 */
	private BigDecimal point ;
	/** 校验码 */
	private String md5Code ;

	/**
	 * 充值金额
	 */
	private BigDecimal rechargePoint;

}
