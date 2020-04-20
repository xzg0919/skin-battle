package com.tzj.collect.common.util;

import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public class CompanyEquipmentUtils {
    public static CompanyEquipment getCompanyEquipment(){

        Subject subject=ApiContext.getSubject();

        if(subject==null){
            throw new ApiException("request中subject为空，确认此接口有token参数传入！");
        }

        //接口里面获取  Member 的例子
        CompanyEquipment equipment= (CompanyEquipment) subject.getUser();

        return equipment;
    }
}
