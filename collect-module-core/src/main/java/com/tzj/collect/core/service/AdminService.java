package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Admin;


public interface AdminService  extends IService<Admin>{

	@DS("slave")
	Admin selectByUserNameAndPwd(String UserName, String Pwd);

	@DS("slave")
	Admin selectByid(Long id);
}
