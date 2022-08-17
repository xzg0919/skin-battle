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
@TableName("roll_room_user")
@Data
public class RollRoomUser extends DataEntity{

	/** ROLL房ID */
	private String roomId ;
	/** 用户ID */
	private String userId ;
	/** 昵称 */
	private String nickName ;
	/** 电话 */
	private String tel ;
	/** 邮箱 */
	private String email ;
	/** roll房皮肤ID */
	private Long rrsiId ;
	/** 皮肤名称 */
	private String skinName ;
	/** 价格 */
	private BigDecimal price ;
	/** 皮肤图片链接 */
	private String picUrl ;

}
