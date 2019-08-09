package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyStreetAppliance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyStreetApplianceMapper extends BaseMapper<CompanyStreetAppliance> {

    /**
     * 根据分类id和街道id查询所属公司
     * @param categoryId
     * @param streetId
     * @return
     */
    Integer selectStreetApplianceCompanyId(@Param("categoryId") Integer categoryId, @Param("streetId") Integer streetId);

    Map<String,Object> companyAreaRanges(String companyId);

    Map<String,Object> adminCompanyAreaRanges(String companyId);
	
	List<Map<String,Object>> getAreaStreetList(@Param("companyId") long companyId, @Param("cityName") String cityName, @Param("areaName") String areaName, @Param("starts") Integer starts, @Param("ends") Integer ends);

    Integer getAreaStreetCount(@Param("companyId") long companyId,@Param("cityName") String cityName,@Param("areaName") String areaName);
}
