package com.tzj.collect.api.adminReceeption;

import com.tzj.collect.core.param.admin.AdminBean;
import com.tzj.collect.core.service.AdminReceptionService;
import com.tzj.collect.core.service.AdminService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

@ApiService
public class AdminReceptionApi {


    @Autowired
    private AdminReceptionService adminReceptionService;

    /**
     * 根据层用户用和密码登录获取token
     * @param
     * @return
     */
    @Api(name = "adminReception.getToken", version = "1.0")
    @SignIgnore
    @AuthIgnore
    //@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getAdminReceptionToken(AdminBean adminBean){
        return adminReceptionService.getAdminReceptionToken(adminBean.getUsername(),adminBean.getPassword());
    }





}
