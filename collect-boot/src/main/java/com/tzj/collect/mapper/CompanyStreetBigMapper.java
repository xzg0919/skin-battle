package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyStreetBig;
import org.apache.ibatis.annotations.Param;

public interface CompanyStreetBigMapper extends BaseMapper<CompanyStreetBig> {

    Integer selectStreetBigCompanyId(@Param("categoryId") Integer categoryId, @Param("streetId")Integer streetId);
}
