package com.skin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.util.List;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("box_battle_user")
@Data
public class BoxBattleUser extends DataEntity{

	/** ROLL房ID */
	private Long boxBattleId ;
	private Long boxId;
	/** 用户ID */
	private Long userId ;
	/** 昵称 */
	private String nickName ;
	/** 电话 */
	private String tel ;
	/** 邮箱 */
	private String email ;
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
	private String avatar;

	@TableField(exist = false)
	private Integer vipLevel ;

	@TableField(exist = false)
	private List<BoxBattleUser> userReward ;

	@TableField(exist = false)
	private Integer winner ;


}
