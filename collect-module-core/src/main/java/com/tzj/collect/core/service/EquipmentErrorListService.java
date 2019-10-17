package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.iot.AdminIotErrorBean;
import com.tzj.collect.core.param.iot.IotErrorParamBean;
import com.tzj.collect.entity.EquipmentErrorList;

import java.util.List;
import java.util.Map;

public interface EquipmentErrorListService extends IService<EquipmentErrorList> {
    Map<String, Object>  iotErrorListByMember(IotErrorParamBean iotErrorParamBean);

    Page<Map<String, Object>> adminIotErrorPage(AdminIotErrorBean adminIotErrorBean);
}
