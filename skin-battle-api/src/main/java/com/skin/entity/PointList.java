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
@TableName("point_list")
@Data
public class PointList extends DataEntity<Long>{
	@TableId(type = IdType.AUTO)
	private  Long id ;

	private String userId;

	/** 订单号 */
	private String orderNo ;
	/** 订单id */
	private Long orderId ;
	/** 订单来源中文 */
	private String orderFromChn ;
	/** 订单来源 */
	private Integer orderFrom ;
	/** 类型 0：扣除 1：新增 */
	private Integer type ;
	/** 积分值 */
	private BigDecimal point ;
	/** 金额 */
	private BigDecimal amount ;
}
