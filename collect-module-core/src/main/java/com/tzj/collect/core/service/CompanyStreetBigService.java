package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStreetBig;

import java.util.Map;

public interface CompanyStreetBigService  extends IService<CompanyStreetBig> {
    @DS("slave")
    Integer selectStreetBigCompanyId(Integer streetId);

    @DS("slave")
    Integer selectStreetBigCompanyIdByCategoryId(Integer categoryId,Integer streetId);

    @DS("slave")
    Map<String,Object> companyAreaRanges(String companyId);
	
	@DS("slave")
    Object getAreaStreetList(long companyId,String cityName,String areaName,Integer starts,Integer ends);
    @DS("slave")
    Object getAreaStreetCount(long companyId,String cityName,String areaName);
}
