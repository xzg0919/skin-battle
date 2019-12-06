package com.tzj.collect.api.admin;


import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.service.CompanyCityRatioService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminCompanyCityRatioApi {

    @Autowired
    private CompanyCityRatioService companyCityRatioService;


    /**
     * 根据城市Id和服务商id更改相关城市系数
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.updateCompanyCityRatio", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String updateCompanyCityRatio(AreaBean areaBean){
        return  companyCityRatioService.updateCompanyCityRatio(areaBean);
    }










}
