package com.tzj.collect.api.admin;

import com.tzj.collect.core.param.admin.AdminBean;
import com.tzj.collect.core.service.AdminService;
import com.tzj.collect.entity.Admin;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminApi {

    @Autowired
    private AdminService adminService;

    /**
     * 根据层用户用和密码登录获取token
     * @param
     * @return
     */
    @Api(name = "admin.getToken", version = "1.0")
    @SignIgnore
    @AuthIgnore
    //@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getToken(AdminBean adminBean){
       return adminService.getToken(adminBean.getUsername(),adminBean.getPassword());
    }
}
