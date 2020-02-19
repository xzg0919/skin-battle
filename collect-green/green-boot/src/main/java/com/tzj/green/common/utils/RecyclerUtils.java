package com.tzj.green.common.utils;

import com.tzj.green.entity.Company;
import com.tzj.green.entity.Recyclers;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;

public class RecyclerUtils {
	
	public static Recyclers getRecyclers() {
		Subject subject=ApiContext.getSubject();
		// 接口里面获取 CompanyAccount 的例子
		Recyclers recyclers = (Recyclers)subject.getUser();
		return recyclers;
	}
}
