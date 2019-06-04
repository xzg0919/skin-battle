package com.tzj.collect.common.util;

import com.tzj.collect.entity.CompanyAccount;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;

public class BusinessUtils {
	
	public static CompanyAccount  getCompanyAccount() {
		Subject subject=ApiContext.getSubject();
		// 接口里面获取 CompanyAccount 的例子
		CompanyAccount companyAccount = (CompanyAccount)subject.getUser();
		return companyAccount;
	}
}
