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
@TableName("cdk_info")
@Data
public class CdkInfo extends DataEntity{

	/** 8位兑换码 */
	@TableField("code_")
	private String code ;
	/** 价值 */
	private BigDecimal cdkVal ;
	/** 兑换次数 */
	private Integer times ;
	/** 是否启用 1：是 0：否 */
	@TableField("enable_")
	private Integer enable ;
}
