package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStreetAppliance;
import com.tzj.collect.entity.CompanyStreetElectroMobile;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyStreetElectroMobileService extends IService<CompanyStreetElectroMobile> {

    Integer selectCompanyByStreetId(Integer streetId);

    Integer selectCompanyByCategoryId(@Param("categoryId") Integer categoryId, @Param("streetId") Integer streetId);

    Map<String,Object> adminCompanyAreaRanges(String companyId);

    Object getAreaStreetList(long companyId,String cityName,String areaName,Integer starts,Integer ends);

    Object getAreaStreetCount(long companyId,String cityName,String areaName);

    Map<String,Object> companyAreaRanges(String companyId);


    void  saveList(List<CompanyStreetElectroMobile> companyStreetElectroMobiles);


    List<Map<String,Object>> getCityId();

}
