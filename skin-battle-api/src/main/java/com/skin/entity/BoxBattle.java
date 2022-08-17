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
@TableName("box_battle")
@Data
public class BoxBattle extends DataEntity{


	/** 皮肤名称 */
	private String boxName ;
	/** 价格 */
	private BigDecimal price ;
	/** 盒子图片 */
	private String boxPic ;
	/** 高中奖概率 */
	private Double highProbability ;
	/** 中中奖概率 */
	private Double middleProbability ;
	/** 低中奖概率 */
	private Double lowProbability ;
	/** 开启状态 0：开启 1：关闭 */
	@TableField("status_")
	private Integer status ;

}
