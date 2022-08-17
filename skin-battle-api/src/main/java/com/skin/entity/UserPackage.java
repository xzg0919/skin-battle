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
@TableName("user_package")
@Data
public class UserPackage extends DataEntity{


	private Long userId;
	/** 是否已取回 0：待取回 1：取回中 2：已取回 */
	private Integer isTake;

	private Long boxId;

	@TableField("from_")
	private Integer from;
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

	private String nickName;

	private String avatar;

}
