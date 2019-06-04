package com.tzj.collect.api.ali;


import com.tzj.collect.api.ali.param.RecruitExpressBean;
import com.tzj.collect.service.RecruitExpressService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;

import org.springframework.beans.factory.annotation.Autowired;

@ApiService
public class RecruitExpressApi {

    @Autowired
    private RecruitExpressService recruitExpressService;


    @Api(name = "recruit.express.save", version = "1.0")
    @SignIgnore
    @AuthIgnore //这个api忽略token验证
    public Object recruitExpressSave(RecruitExpressBean recruitExpressBean) {
        return recruitExpressService.recruitExpressSave(recruitExpressBean);
    }
}
