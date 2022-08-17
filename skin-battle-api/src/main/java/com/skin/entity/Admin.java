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
@TableName("admin")
@Data
public class Admin extends DataEntity  {

	/** 账号 */

	private String userCode ;
	/** 密码 */

	private String userPswd ;


	private String userName;
}
