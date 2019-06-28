package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStreetAppliance;

import java.util.List;
import java.util.Map;

public interface CompanyStreetApplianceService extends IService<CompanyStreetAppliance> {
    /**
     * 根据分类id和街道id查询所属公司
     * @param categoryId
     * @param streetId
     * @return
     */
    @DS("slave")
    String selectStreetApplianceCompanyId(Integer categoryId, Integer streetId,Integer communityId);
    /**
     * 根据分类id和街道id查询所属公司
     * @param categoryId
     * @param streetId
     * @return
     */
    @DS("slave")
    String selectStreetApplianceCompanyId(Integer categoryId, Integer streetId);

    @DS("slave")
    Map<String,Object> companyAreaRanges(String companyId);

    @DS("slave")
    Map<String,Object> adminCompanyAreaRanges(String companyId);
}
