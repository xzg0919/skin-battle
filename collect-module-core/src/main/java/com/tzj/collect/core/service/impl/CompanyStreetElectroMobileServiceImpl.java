package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyStreetApplianceMapper;
import com.tzj.collect.core.mapper.CompanyStreetElectroMobileMapper;
import com.tzj.collect.core.service.CompanyCategoryService;
import com.tzj.collect.core.service.CompanyStreetApplianceService;
import com.tzj.collect.core.service.CompanyStreetElectroMobileService;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyStreetAppliance;
import com.tzj.collect.entity.CompanyStreetElectroMobile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true)
public class CompanyStreetElectroMobileServiceImpl extends ServiceImpl<CompanyStreetElectroMobileMapper, CompanyStreetElectroMobile> implements CompanyStreetElectroMobileService {

    @Autowired
    private CompanyStreetElectroMobileMapper companyStreetElectroMobileMapper;


    @Override
    public Integer selectCompanyByStreetId(Integer streetId) {
        return companyStreetElectroMobileMapper.selectCompanyByStreetId(streetId);
    }

    @Override
    public Integer selectCompanyByCategoryId(Integer categoryId, Integer streetId) {
        return companyStreetElectroMobileMapper.selectStreetCompanyIdByCategoryId(categoryId,streetId);
    }

    @Override
    public Map<String, Object> adminCompanyAreaRanges(String companyId) {
        return companyStreetElectroMobileMapper.adminCompanyAreaRanges(companyId);
    }

    @Override
    public Object getAreaStreetList(long companyId, String cityName, String areaName, Integer starts, Integer ends) {
        return companyStreetElectroMobileMapper.getAreaStreetList(companyId,cityName,areaName,starts,ends);
    }

    @Override
    public Object getAreaStreetCount(long companyId, String cityName, String areaName) {
        return companyStreetElectroMobileMapper.getAreaStreetCount(companyId,cityName,areaName);
    }

    @Override
    public Map<String,Object> companyAreaRanges(String companyId) {
        return companyStreetElectroMobileMapper.companyAreaRanges(companyId);
    }

    @Transactional
    @Override
    public void saveList(List<CompanyStreetElectroMobile> companyStreetElectroMobiles) {
        this.insertBatch(companyStreetElectroMobiles);
    }

    @Override
    public List<Map<String, Object>> getCityId() {
        return companyStreetElectroMobileMapper.getCityId();
    }
}
