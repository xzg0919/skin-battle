package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyStree;
import com.tzj.collect.entity.CompanyStreetAppSmall;
import org.apache.ibatis.annotations.Param;

public interface CompanyStreetAppSmallMapper extends BaseMapper<CompanyStreetAppSmall> {



    Integer selectStreetAppSmallCompanyIds(@Param("streetId") Integer streetId);


}
