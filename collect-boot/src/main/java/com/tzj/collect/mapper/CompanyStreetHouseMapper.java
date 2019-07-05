package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyStreetHouse;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CompanyStreetHouseMapper extends BaseMapper<CompanyStreetHouse> {


    /**
     * 根据分类id和街道id查询所属公司
     * @param categoryId
     * @param streetId
     * @return
     */
    Integer selectStreetHouseCompanyId(@Param("categoryId") Integer categoryId, @Param("streetId")Integer streetId);

    Map<String,Object> adminCompanyAreaRanges(String companyId);

    Map<String,Object> companyAreaRanges(String companyId);



}
