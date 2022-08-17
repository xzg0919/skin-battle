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
@TableName("pull_box_skin")
@Data
public class PullBoxSkin extends DataEntity{


	private Long pullBoxId;
	/** 皮肤名称 */
	private String skinName;
	/** 磨损度 */
	private String attritionRate ;
	/** 品级 */
	@TableField("level_")
	private Integer level ;
	/** 价格 */
	private BigDecimal price ;
	/** 图片链接 */
	private String picUrl ;
	/** 中奖概率 */
	private Double probability ;

}
