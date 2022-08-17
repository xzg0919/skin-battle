package com.skin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("take_order")
@Data
public class TakeOrder extends DataEntity{



	/** 订单号 **/
	String orderNo;

	Long  userId;
	/** 来源 */
	private Integer source ;
	/** 皮肤名称 */
	private String skinName ;
	/** 昵称 */
	private String nickName ;
	/** 邮箱 */
	private String email ;
	/** 电话 */
	private String tel ;
	/** 交易链接 */
	private String steamUrl ;
	/** 状态 0：代发货 1：已发货 2：已驳回 */
	@TableField("status_")
	private Integer status ;
	/** 发货时间 */
	private Date takeTime ;
	/** 价格 */
	private BigDecimal price ;

}
