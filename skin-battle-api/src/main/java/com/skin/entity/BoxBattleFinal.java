package com.skin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("box_battle_final")
@Data
public class BoxBattleFinal extends DataEntity{

	/** ROLL房ID */
	private Long boxBattleId ;
	/** 用户ID */
	private Long userId ;
	/** 皮肤名称 */
	private String skinName ;
	/** 价格 */
	private BigDecimal price ;
	/** 皮肤图片链接 */
	private String picUrl ;
	/** 品级  稀有级别 0 初级 1 中级 2高级 */
	@TableField("level_")
	private Integer level ;
	/** 磨损度 */
	private String attritionRate ;


}
