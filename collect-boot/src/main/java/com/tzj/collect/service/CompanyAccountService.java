package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.business.param.CompanyAccountBean;
import com.tzj.collect.api.param.TokenBean;
import com.tzj.collect.entity.CompanyAccount;

public interface CompanyAccountService  extends IService<CompanyAccount>{
	
	CompanyAccount selectByUsername(String userName,String passWord);

	TokenBean login(CompanyAccountBean accountBean);
	 
}
