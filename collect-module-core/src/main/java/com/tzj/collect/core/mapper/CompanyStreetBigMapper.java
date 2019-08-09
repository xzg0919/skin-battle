package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyStreetBig;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyStreetBigMapper extends BaseMapper<CompanyStreetBig> {

    Integer selectStreetBigCompanyId(@Param("categoryId") Integer categoryId, @Param("streetId") Integer streetId);

    Map<String,Object> companyAreaRanges(String companyId);
	
	List<Map<String,Object>> getAreaStreetList(@Param("companyId") long companyId, @Param("cityName") String cityName, @Param("areaName") String areaName, @Param("starts") Integer starts, @Param("ends") Integer ends);

    Integer getAreaStreetCount(@Param("companyId") long companyId,@Param("cityName") String cityName,@Param("areaName") String areaName);
}
