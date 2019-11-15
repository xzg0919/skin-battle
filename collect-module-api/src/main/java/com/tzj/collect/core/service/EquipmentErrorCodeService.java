package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.iot.IotErrorParamBean;
import com.tzj.collect.entity.EquipmentErrorCode;

import java.util.List;
import java.util.Map;

public interface EquipmentErrorCodeService extends IService<EquipmentErrorCode> {
    List<Map<String, Object>> iotErrorCode(IotErrorParamBean iotErrorParamBean);
}
