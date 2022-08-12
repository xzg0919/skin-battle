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
@TableName("point_info")
@Data
public class PointInfo extends DataEntity<Long>{
	@TableId(type = IdType.AUTO)
	private  Long id ;
	/** userid */
	private String userId ;

	/** 总积分 */
	private BigDecimal totalPoint ;
	/** 消纳积分 */
	private BigDecimal consumePoint ;
	/** 剩余积分 */
	private BigDecimal point ;
	/** 校验码 */
	private String md5Code ;

}
