package com.tzj.collect.common.util;

import javax.servlet.http.HttpServletRequest;

import com.tzj.collect.entity.Recyclers;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;

public class RecyclersUtils {
	// 接口里面获取 Recyclers
		public static Recyclers getRecycler() {
			Subject subject=ApiContext.getSubject();
			 if(subject==null){
		            throw new ApiException("request中subject为空，确认此接口有token参数传入！");
		        }
			Recyclers recyclers = (Recyclers) subject.getUser();
			return recyclers;
		}
}
