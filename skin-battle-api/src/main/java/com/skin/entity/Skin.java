package com.skin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.function.Function;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("skin")
@Data
public class Skin extends DataEntity<Long>{

	@TableId(type = IdType.AUTO)
	private  Long id ;
	/** 名称 */
	@TableField("name_")
	private String name ;
	/** 品级  稀有级别 0 初级 1 中级 2高级 */
	@TableField("level_")
	private Integer level ;
	/** 磨损度 */
	private String attritionRate ;
	/** 价格 */
	private BigDecimal price ;
	/** 图片链接 */
	private String picUrl ;


}
