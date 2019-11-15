package com.tzj.collect.common.util;

import com.tzj.collect.entity.EnterpriseTerminal;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;

public class EnterpriseTerminalUtils {

    public static EnterpriseTerminal getEnterpriseTerminal() {
        Subject subject= ApiContext.getSubject();
        if(subject==null){
            throw new ApiException("request中subject为空，确认此接口有token参数传入！");
        }
        // 接口里面获取 CompanyAccount 的例子
        EnterpriseTerminal enterpriseTerminal = (EnterpriseTerminal)subject.getUser();
        return enterpriseTerminal;
    }
}
