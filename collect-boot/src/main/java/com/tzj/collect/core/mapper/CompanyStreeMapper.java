package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyStree;
import org.apache.ibatis.annotations.Param;

public interface CompanyStreeMapper extends BaseMapper<CompanyStree> {



    Integer selectStreeCompanyIds(@Param("categoryId") Integer categoryId, @Param("streetId") Integer streetId);


}
