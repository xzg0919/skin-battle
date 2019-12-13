package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.iot.AdminIotErrorBean;
import com.tzj.collect.entity.CompanyEquipment;

import java.util.List;
import java.util.Map;

public interface CompanyEquipmentService extends IService<CompanyEquipment> {
    @DS("slave")
    Page<Map<String, Object>> adminIotOrderPage(AdminIotErrorBean adminIotErrorBean);
    @DS("slave")
    String iotConRate();
    @DS("slave")
    List<Map<String, Object>> adminIotOrderList(AdminIotErrorBean adminIotBean);
}
