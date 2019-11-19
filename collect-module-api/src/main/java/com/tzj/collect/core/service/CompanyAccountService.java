package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.business.CompanyAccountBean;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.collect.entity.CompanyAccount;

public interface CompanyAccountService  extends IService<CompanyAccount>{
	
	CompanyAccount selectByUsername(String userName, String passWord);

	@DS("slave")
	TokenBean login(CompanyAccountBean accountBean);
	 
}
