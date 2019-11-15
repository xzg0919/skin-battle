package com.tzj.collect.core.service;


import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.AbstractEntity;
import com.tzj.collect.entity.EquipmentAdvert;

import java.util.List;
import java.util.Map;

public interface EquipmentAdvertService extends IService<EquipmentAdvert> {
    List<Map<String, Object>> iotEquipmentAdvertList();
}
