package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStreetAppliance;

import java.util.Map;

public interface CompanyStreetApplianceService extends IService<CompanyStreetAppliance> {
    /**
     * 根据回收类型和街道id查询所属公司
     * @param streetId
     * @return
     */
    @DS("slave")
    String selectStreetApplianceCompanyId(Integer streetId, Integer communityId);
    /**
     * 根据分类id和街道id查询所属公司
     * @param streetId
     * @return
     */
    @DS("slave")
    String selectStreetApplianceCompanyIdByCategoryId(Integer categoryId,Integer streetId, Integer communityId);

    @DS("slave")
    Map<String,Object> companyAreaRanges(String companyId);
    @DS("slave")
    Map<String,Object> adminCompanyAreaRanges(String companyId);
	@DS("slave")
    Object getAreaStreetList(long companyId,String cityName,String areaName,Integer starts,Integer ends);
    @DS("slave")
    Object getAreaStreetCount(long companyId,String cityName,String areaName);
}
