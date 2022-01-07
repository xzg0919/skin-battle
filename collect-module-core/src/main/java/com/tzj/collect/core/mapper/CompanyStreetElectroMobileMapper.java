package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyStreetAppliance;
import com.tzj.collect.entity.CompanyStreetElectroMobile;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyStreetElectroMobileMapper extends BaseMapper<CompanyStreetElectroMobile> {

    /**
     * 根据街道id查询所属公司
     * @param streetId
     * @return
     */
    Integer selectCompanyByStreetId(@Param("streetId") Integer streetId);


    Integer selectStreetCompanyIdByCategoryId(@Param("categoryId") Integer categoryId,@Param("streetId") Integer streetId);

    Map<String,Object> adminCompanyAreaRanges(String companyId);

    List<Map<String,Object>> getAreaStreetList(@Param("companyId") long companyId, @Param("cityName") String cityName, @Param("areaName") String areaName, @Param("starts") Integer starts, @Param("ends") Integer ends);

    Integer getAreaStreetCount(@Param("companyId") long companyId,@Param("cityName") String cityName,@Param("areaName") String areaName);

    Map<String,Object> companyAreaRanges(String companyId);

    List<Map<String,Object>> getCityId();


}
