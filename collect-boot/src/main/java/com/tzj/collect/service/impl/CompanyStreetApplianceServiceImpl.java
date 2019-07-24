package com.tzj.collect.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyStreetAppliance;
import com.tzj.collect.mapper.CompanyStreetApplianceMapper;
import com.tzj.collect.service.CompanyCategoryService;
import com.tzj.collect.service.CompanyStreetApplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true)
public class CompanyStreetApplianceServiceImpl extends ServiceImpl<CompanyStreetApplianceMapper, CompanyStreetAppliance> implements CompanyStreetApplianceService {

    @Autowired
    private CompanyStreetApplianceMapper companyStreetApplianceMapper;

    @Autowired
    private CompanyCategoryService companyCategoryService;



    /**
     * 根据分类id和街道id查询所属公司
     * @param categoryId
     * @param streetId
     * @return
     */
    @Override
    public String selectStreetApplianceCompanyId(Integer categoryId, Integer streetId,Integer communityId) {
        String companyId = "";
        //根据分类Id和小区Id查询所属企业
        Company companys = companyCategoryService.selectCompany(categoryId,communityId);
        if(companys == null) {
            //根据分类Id和小区id去公海查询相关企业
            Integer companyId1 = companyStreetApplianceMapper.selectStreetApplianceCompanyId(categoryId, streetId);
            if(companyId1==null) {
                return companyId;
            }
            companyId = companyId1.toString();
        }else {
            companyId = companys.getId().toString();
        }
        return companyId;

    }

    @Override
    public String selectStreetApplianceCompanyId(Integer categoryId, Integer streetId) {
        String companyId = "";
        //根据分类Id和小区id去公海查询相关企业
        Integer companyId1 = companyStreetApplianceMapper.selectStreetApplianceCompanyId(categoryId, streetId);
        if(companyId1==null) {
            return null;
        }
        return companyId1.toString();
    }

    @Override
    public Map<String,Object> companyAreaRanges(String companyId) {
        return companyStreetApplianceMapper.companyAreaRanges(companyId);
    }

    @Override
    public Map<String,Object> adminCompanyAreaRanges(String companyId) {
        return companyStreetApplianceMapper.adminCompanyAreaRanges(companyId);
    }

    @Override
    public Object getAreaStreetList(long companyId, String cityName, String areaName, Integer starts, Integer ends) {
        return companyStreetApplianceMapper.getAreaStreetList(companyId,cityName,areaName,starts,ends);
    }

    @Override
    public Object getAreaStreetCount(long companyId, String cityName, String areaName) {
        return companyStreetApplianceMapper.getAreaStreetCount(companyId,cityName,areaName);
    }
}
