package com.skin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tzj.module.common.utils.DateUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("roll_room")
@Data
public class RollRoom extends DataEntity{

	@TableField("name_")
	/** 房间名称 */
	private String name ;
	/** 房间类型 0:官方房间 1：福利房间 */
	private Integer roomType ;
	/** 开奖时间 */
	private Date lotteryTime ;
	/** 参与条件 0：充值 1：口令 */
	private Integer conditionType ;
	/** 口令 */
	private String roomPswd ;
	/** 充值金额 */
	private BigDecimal price ;
	/** 房间描述 */
	@TableField("desc_")
	private String desc ;
	/** 房间图片 */
	private String roomPic ;

	/** 房间状态  1：待开奖 2：已结束 */
	Integer roomStatus ;

	public String getLotteryTimeStr() {
		return getDate(lotteryTime);
	}

	@TableField(exist = false)
	String lotteryTimeStr;


}
