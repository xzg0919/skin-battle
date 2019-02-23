package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.LogisticsCompany;
import org.apache.ibatis.annotations.Param;

public interface LogisticsCompanyMapper extends BaseMapper<LogisticsCompany> {

    Integer selectLogisticeCompanyIds(@Param("categoryId") Integer categoryId,@Param("streeId")  Integer streeId);

}
