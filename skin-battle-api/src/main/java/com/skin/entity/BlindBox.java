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
@TableName("blind_box")
@Data
public class BlindBox extends DataEntity<Long>{
	@TableId(type = IdType.AUTO)
	private  Long id ;
	/** 皮肤名称 */
	private String boxName ;
	/** 价格 */
	private BigDecimal price ;
	/** 磨损度 */
	private String attritionRate ;
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

}
