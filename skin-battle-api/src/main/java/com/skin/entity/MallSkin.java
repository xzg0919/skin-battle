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
@TableName("mall_skin")
@Data
public class MallSkin extends DataEntity{


	/** 商品名称 */
	private String goodsName ;
	/** 库存 */
	private Integer stock ;
	/** 总库存 */
	private Integer totalStock ;
	/** 消纳库存 */
	private Integer consumeStock ;
	/** 磨损度 */
	private String attritionRate ;
	/** 价格 */
	private BigDecimal price ;
	/** 图片链接 */
	private String goodsPic ;

}
