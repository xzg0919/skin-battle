package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.business.param.CompanyAccountBean;
import com.tzj.collect.api.param.TokenBean;
import com.tzj.collect.entity.CompanyAccount;

public interface CompanyAccountService  extends IService<CompanyAccount>{
	
	CompanyAccount selectByUsername(String userName,String passWord);

	@DS("slave")
	TokenBean login(CompanyAccountBean accountBean);
	 
}
