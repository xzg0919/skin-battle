package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyStreetAppliance;
import com.tzj.collect.mapper.CompanyStreetApplianceMapper;
import com.tzj.collect.service.CompanyCategoryService;
import com.tzj.collect.service.CompanyStreetApplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
