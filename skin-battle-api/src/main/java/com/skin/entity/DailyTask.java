package com.skin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("daily_task")
@Data
public class DailyTask extends DataEntity<Long>{
	@TableId(type = IdType.AUTO)
	private  Long id ;
	/** 标题 */
	private String title ;
	/** 状态 0：启用 1：禁用 */
	@TableField("status_")
	private Integer status ;
	/** 任务类型 1：充值  0：消费 */
	private Integer type ;
	/** 金额 */
	private BigDecimal price ;
	/** 获得的金额 */
	private BigDecimal rewardPrice ;
}
