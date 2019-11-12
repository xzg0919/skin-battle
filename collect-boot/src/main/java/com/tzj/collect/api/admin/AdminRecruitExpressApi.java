package com.tzj.collect.api.admin;

import com.tzj.collect.core.param.ali.RecruitExpressBean;
import com.tzj.collect.core.service.RecruitExpressService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminRecruitExpressApi {
    @Autowired
    private RecruitExpressService recruitExpressService;

    /**
     * 根据条件获取招募的内容
     * @param
     * @return
     */
    @Api(name = "recruit.getRecruitList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getRecruitList(RecruitExpressBean recruitExpressBean){
        return recruitExpressService.getRecruitList(recruitExpressBean);
    }


}

