package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyStreetHouseMapper;
import com.tzj.collect.core.service.CompanyCategoryService;
import com.tzj.collect.core.service.CompanyStreetHouseService;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyStreetHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly=true)
public class CompanyStreetHouseServiceImpl extends ServiceImpl<CompanyStreetHouseMapper, CompanyStreetHouse> implements CompanyStreetHouseService {

    @Autowired
    private CompanyStreetHouseMapper companyStreetHouseMapper;
    @Autowired
    private CompanyCategoryService companyCategoryService;


    @Override
    public String selectStreetHouseceCompanyId(Integer categoryId, Integer streetId, Integer communityId) {
        String companyId = "";
        //根据分类Id和小区Id查询所属企业
        Company companys = companyCategoryService.selectCompany(categoryId,communityId);
        if(companys == null) {
            //根据分类Id和小区id去公海查询相关企业
            Integer companyId1 = companyStreetHouseMapper.selectStreetHouseCompanyId(categoryId, streetId);
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
    public Map<String, Object> adminCompanyAreaRanges(String companyId) {
        return companyStreetHouseMapper.adminCompanyAreaRanges(companyId);
    }

    @Override
    public Map<String,Object> companyAreaRanges(String companyId) {
        return companyStreetHouseMapper.companyAreaRanges(companyId);
    }

    @Override
    public Integer selectStreetHouseceCompanyId(Integer categoryId, Integer streetId) {

        return companyStreetHouseMapper.selectStreetHouseCompanyId(categoryId, streetId);
    }
}
