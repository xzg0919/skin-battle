package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.EquipmentLocationListMapper;
import com.tzj.collect.core.service.EquipmentLocationListService;
import com.tzj.collect.entity.EquipmentLocationList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * iot设备定位
 * @author: sgmark@aliyun.com
 * @Date: 2019/12/11 0011
 * @Param: 
 * @return: 
 */
@Service
@Transactional(readOnly = true)
public class EquipmentLocationListServiceImpl extends ServiceImpl<EquipmentLocationListMapper, EquipmentLocationList> implements EquipmentLocationListService {
	

}
