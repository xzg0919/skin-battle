package com.skin.api.admin;

import com.skin.core.service.AdminService;
import com.skin.params.AdminBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

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
    public Object getToken(AdminBean adminBean){
       return adminService.getToken(adminBean.getUserCode(),adminBean.getPassword());
    }



}
