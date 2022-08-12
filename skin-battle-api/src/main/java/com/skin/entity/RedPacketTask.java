package com.skin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("red_packet_task")
@Data
public class RedPacketTask extends DataEntity<Long>{
	@TableId(type = IdType.AUTO)
	private  Long id ;
	/** 充值金额 */
	private BigDecimal price ;
	/** 标题 */
	private String title ;
	/** 开始时间 */
	private Date startTime ;
	/** 结束时间 */
	private Date endTime ;
	/** 红包金额开始 */
	private BigDecimal redPacketBegin ;
	/** 红包金额截止 */
	private BigDecimal redPacketEnd ;
	/** 状态 0：正常 1：停用 */
	@TableField("status_")
	private Integer status;
}
