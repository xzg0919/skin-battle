package com.tzj.collect.api.business;

import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.core.param.business.CompanyAccountBean;
import com.tzj.collect.core.service.CompanyAccountService;
import com.tzj.collect.core.service.CompanyService;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class BusinessLoginApi {
	
	/**
	 * 废弃，调用之前的business.token.get
	 */
	@Autowired
	private CompanyAccountService accountService;
	
	@Autowired
	private CompanyService companyService;
	
	@Api(name = "business.login", version = "1.0")
	@SignIgnore
	@AuthIgnore //这个api忽略token验证
	//@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public TokenBean Login(CompanyAccountBean accountBean) {
		return accountService.login(accountBean);
	}
	@Api(name = "business.company.current", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Company selectCurrent() {
		// 接口里面获取 CompanyAccount 的例子
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return companyService.getCurrent(companyAccount);
	}
}
