package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
 * @ClassName: Admin 
 * @author: 向忠国
 * @date: 2018年3月16日 上午11:04:44
 */
@TableName("sb_admin")
public class Admin extends DataEntity<Long> {
	private Long id;
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String username;   //账号
	private String password;   //密码
	@TableField(value="name_")
	private String name;      //名称

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
