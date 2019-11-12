package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyStreetApplianceMapper;
import com.tzj.collect.core.service.CompanyCategoryService;
import com.tzj.collect.core.service.CompanyStreetApplianceService;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyStreetAppliance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly=true)
public class CompanyStreetApplianceServiceImpl extends ServiceImpl<CompanyStreetApplianceMapper, CompanyStreetAppliance> implements CompanyStreetApplianceService {

    @Autowired
    private CompanyStreetApplianceMapper companyStreetApplianceMapper;

    @Autowired
    private CompanyCategoryService companyCategoryService;



    /**
     * 根据回收类型和街道id查询所属公司
     * @param streetId
     * @return
     */
    @Override
    public String selectStreetApplianceCompanyId(Integer streetId,Integer communityId) {
        String companyId = "";
        //根据回收类型和小区Id查询所属企业
        Company companys = companyCategoryService.selectCompanyByTitle("1",communityId);
        if(companys == null) {
            //根据街道id去公海查询相关企业
            Integer companyId1 = companyStreetApplianceMapper.selectStreetApplianceCompanyId( streetId);
            if(companyId1==null) {
                return companyId;
            }
            companyId = companyId1.toString();
        }else {
            companyId = companys.getId().toString();
        }
        return companyId;
    }
    /**
     * 根据分类id和街道id查询所属公司
     * @param streetId
     * @return
     */
    @Override
    public String selectStreetApplianceCompanyIdByCategoryId(Integer categoryId,Integer streetId,Integer communityId) {
        String companyId = "";
        //根据回收类型和小区Id查询所属企业
        Company companys = companyCategoryService.selectCompanys(categoryId,communityId);
        if(companys == null) {
            //根据街道id去公海查询相关企业
            Integer companyId1 = companyStreetApplianceMapper.selectStreetApplianceCompanyIdByCategoryId(categoryId,streetId);
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
