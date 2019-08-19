package com.tzj.point.common.util;

import com.tzj.collect.entity.Recyclers;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.point.entity.JfappRecycler;

public class JfappRecyclerUtils {
	// 接口里面获取 Recyclers
		public static JfappRecycler getRecycler() {
			Subject subject=ApiContext.getSubject();
			 if(subject==null){
		            throw new ApiException("request中subject为空，确认此接口有token参数传入！");
		        }
			JfappRecycler jfappRecycler = (JfappRecycler) subject.getUser();
			return jfappRecycler;
		}
}
