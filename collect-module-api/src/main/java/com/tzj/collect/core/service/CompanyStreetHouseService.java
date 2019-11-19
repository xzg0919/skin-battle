package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStreetHouse;

import java.util.Map;

public interface CompanyStreetHouseService extends IService<CompanyStreetHouse> {


    @DS("slave")
    String selectStreetHouseceCompanyId(Integer streetId, Integer communityId);
    @DS("slave")
    String selectStreetHouseceCompanyIdByCategoryId(Integer categoryId,Integer streetId, Integer communityId);
    @DS("slave")
    Integer selectStreetHouseCompanyIdByCategoryId(Integer categoryId,Integer streetId);
    @DS("slave")
    Map<String,Object> adminCompanyAreaRanges(String companyId);
    @DS("slave")
    Map<String,Object> companyAreaRanges(String companyId);
	@DS("slave")
    Object getAreaStreetList(long companyId,String cityName,String areaName,Integer starts,Integer ends);
    @DS("slave")
    Object getAreaStreetCount(long companyId,String cityName,String areaName);
}
