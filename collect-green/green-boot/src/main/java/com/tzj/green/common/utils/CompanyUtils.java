package com.tzj.green.common.utils;

import com.tzj.green.entity.Company;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;

public class CompanyUtils {
	
	public static Company getCompanyAccount() {
		Subject subject=ApiContext.getSubject();
		// 接口里面获取 CompanyAccount 的例子
		Company company = (Company)subject.getUser();
		return company;
	}
}
