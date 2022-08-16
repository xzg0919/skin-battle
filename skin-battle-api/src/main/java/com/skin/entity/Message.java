package com.skin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("message")
@Data
public class Message extends DataEntity<Long>{
	@TableId(type = IdType.AUTO)
	private  Long id ;

	private String title ;

	private String content ;

	private Long userId;

	/** 是否已读 0： 未读  1：已读 */
	private Integer isRead;
}
