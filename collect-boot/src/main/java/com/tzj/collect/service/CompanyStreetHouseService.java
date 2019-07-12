package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStreetHouse;

import java.util.Map;

public interface CompanyStreetHouseService extends IService<CompanyStreetHouse> {


    @DS("slave")
    String selectStreetHouseceCompanyId(Integer categoryId, Integer streetId,Integer communityId);

    @DS("slave")
    Map<String,Object> adminCompanyAreaRanges(String companyId);
    @DS("slave")
    Map<String,Object> companyAreaRanges(String companyId);

    @DS("slave")
    Integer selectStreetHouseceCompanyId(Integer categoryId, Integer streetId);
}
