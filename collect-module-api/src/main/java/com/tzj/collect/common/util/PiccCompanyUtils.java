package com.tzj.collect.common.util;

import com.tzj.collect.entity.PiccCompany;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;

public class PiccCompanyUtils {
    public static PiccCompany getPiccCompany(){

        Subject subject= ApiContext.getSubject();

        if(subject==null){
            throw new ApiException("request中subject为空，确认此接口有token参数传入！");
        }

        //接口里面获取  Member 的例子
        PiccCompany piccCompany= (PiccCompany) subject.getUser();

        return piccCompany;
    }
}
