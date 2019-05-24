package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.CompanyStreetAppliance;
import com.tzj.collect.mapper.CompanyStreetApplianceMapper;
import com.tzj.collect.service.CompanyStreetApplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class CompanyStreetApplianceServiceImpl extends ServiceImpl<CompanyStreetApplianceMapper, CompanyStreetAppliance> implements CompanyStreetApplianceService {

    @Autowired
    private CompanyStreetApplianceMapper companyStreetApplianceMapper;


}
