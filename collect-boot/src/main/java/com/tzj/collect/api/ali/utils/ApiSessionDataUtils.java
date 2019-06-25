package com.tzj.collect.api.ali.utils;

import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;

/**
 * @Author 胡方明(12795880 @ qq.com)
 * 哆哆生鲜
 */
public class ApiSessionDataUtils {
    public static ApiSessionData getApiSessionData(){
        Subject subject= ApiContext.getSubject();
        if(subject==null){
            throw new ApiException("request中subject为空，确认此接口有token参数传入！");
        }

        //为了兼容旧版本===
        ApiSessionData apiSessionData= (ApiSessionData) subject.getNewSessionData();
        return apiSessionData;
    }
}
