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
@TableName("blind_box")
@Data
public class BlindBox extends DataEntity{

	/** 皮肤名称 */
	private String boxName ;

	/** 盒子类型 **/
	private Long boxType;
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
	/** 折扣 */
	private Integer discount ;
	/** 皮肤图片 */
	private String skinPic ;

	/** 是否启用 1：是 0：否 */
	@TableField("enable_")
	private Integer enable ;

	/** 折后价格 **/
	private BigDecimal discountPrice ;
}
