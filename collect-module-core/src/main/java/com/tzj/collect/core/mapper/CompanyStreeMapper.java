package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyStree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyStreeMapper extends BaseMapper<CompanyStree> {



    Integer selectStreeCompanyIds(@Param("categoryId") Integer categoryId, @Param("streetId") Integer streetId);

    List<String> findCompanyCityName(@Param("companyId") Long companyId);
}
