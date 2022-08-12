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
@TableName("user")
@Data
public class User extends DataEntity<Long>{
	@TableId(type = IdType.AUTO)
	private  Long id ;
	/** 电话 */
	private String tel ;
	/** 邮箱 */
	private String email ;
	/** 昵称 */
	private String nickName ;
	/** 用户ID */
	private String userId ;
	/** steam交易链接 */
	private String steamUrl ;
	/** 推广码 */
	private String promoCode ;
	/** 头像 */
	private String avatar ;
	/** 高中奖概率 */
	private Double highProbability ;
	/** 中中奖概率 */
	private Double middleProbability ;
	/** 低中奖概率 */
	private Double lowProbability ;
	/** steamID */
	private String steamId ;
	/** vip等级 */
	private Integer vip ;
}
