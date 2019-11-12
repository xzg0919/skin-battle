package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyEquipmentMapper;
import com.tzj.collect.core.service.CompanyEquipmentService;
import com.tzj.collect.entity.CompanyEquipment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sgmark
 * @create 2019-04-02 15:25
 **/
@Service
@Transactional(readOnly = true)
public class CompanyEquipmentServiceImpl extends ServiceImpl<CompanyEquipmentMapper, CompanyEquipment> implements CompanyEquipmentService {
}
