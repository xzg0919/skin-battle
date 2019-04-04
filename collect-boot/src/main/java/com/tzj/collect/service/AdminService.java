package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Admin;


public interface AdminService  extends IService<Admin>{

	Admin selectByUserNameAndPwd(String UserName,String Pwd);
	
	Admin selectByid(Long id);
}
