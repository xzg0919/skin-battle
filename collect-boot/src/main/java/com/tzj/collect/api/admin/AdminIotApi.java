package com.tzj.collect.api.admin;

import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.core.param.iot.AdminIotErrorBean;
import com.tzj.collect.core.param.iot.IotErrorParamBean;
import com.tzj.collect.core.service.EquipmentErrorListService;
import com.tzj.collect.entity.EquipmentErrorList;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

@ApiService
public class AdminIotApi {

    @Resource
    private EquipmentErrorListService equipmentErrorListService;

    @Api(name = "admin.iot.error.page", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Page<Map<String, Object>> adminIotErrorPage(AdminIotErrorBean adminIotErrorBean){
        return equipmentErrorListService.adminIotErrorPage(adminIotErrorBean);
    }
}
