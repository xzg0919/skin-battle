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
@TableName("roll_room_skin_info")
@Data
public class RollRoomSkinInfo extends DataEntity{

	/** 房间id */
	private Long roomId ;
	/** 皮肤图片 */
	private String picUrl ;
	/** 皮肤名称 */
	private String skinName ;
	/** 指定中奖用户 */
	private String specifiedUser ;
	/** 价格 */
	private BigDecimal price ;
	/** 品级  稀有级别 0 初级 1 中级 2高级 */
	@TableField("level_")
	private Integer level ;
	/** 磨损度 */
	private String attritionRate ;

}
