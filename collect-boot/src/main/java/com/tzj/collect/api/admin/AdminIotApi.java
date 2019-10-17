package com.tzj.collect.api.admin;

import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.core.param.iot.AdminIotErrorBean;
import com.tzj.collect.core.service.EquipmentErrorListService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminIotApi {

    @Resource
    private EquipmentErrorListService equipmentErrorListService;

    @Api(name = "admin.iot.error.page", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Page<Map<String, Object>> adminIotErrorPage(AdminIotErrorBean adminIotErrorBean){
        return equipmentErrorListService.adminIotErrorPage(adminIotErrorBean);
    }
}
