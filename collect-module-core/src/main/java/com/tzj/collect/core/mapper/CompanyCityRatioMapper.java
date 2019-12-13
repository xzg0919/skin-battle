package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyCityRatio;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface CompanyCityRatioMapper extends BaseMapper<CompanyCityRatio> {


    BigDecimal getCityRatioByCompanyCityId(@Param("cityId") Integer cityId,@Param("companyId") String companyId);

}
