package com.tzj.collect.api.admin;

import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.core.param.iot.AdminIotErrorBean;
import com.tzj.collect.core.service.CompanyEquipmentService;
import com.tzj.collect.core.service.EquipmentErrorListService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminIotApi {

    @Resource
    private EquipmentErrorListService equipmentErrorListService;

    @Resource
    private CompanyEquipmentService companyEquipmentService;

    /**
     * iot申诉报错查询
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/13 0013
     * @Param: 
     * @return: 
     */
    @Api(name = "admin.iot.error.page", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Page<Map<String, Object>> adminIotErrorPage(AdminIotErrorBean adminIotErrorBean){
        return equipmentErrorListService.adminIotErrorPage(adminIotErrorBean);
    }
    /**
     *查询iot设备订单数人数统计
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/13 0013
     * @Param:
     * @return:
     */
    @Api(name = "admin.iot.order.page", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String, Object> adminIotOrderPage(AdminIotErrorBean adminIotErrorBean){
        Map<String, Object> returnMap = new HashMap<>();
        /**
         * 1、查询转化率（近15天）
         */
        returnMap.put("iotConRate", companyEquipmentService.iotConRate());
        /**
         * 2、查询设备统计
         */
        returnMap.put("iotInfo",  companyEquipmentService.adminIotOrderPage(adminIotErrorBean));
        return returnMap;
    }
    
}
