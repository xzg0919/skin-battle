package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.entity.CompanyCategoryCityLocale;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyCategoryCityLocaleMapper extends BaseMapper<CompanyCategoryCityLocale> {

    List<CategoryResult> getHouseHoldDetailLocale(@Param("parentId") String parentId, @Param("companyId") String companyId, @Param("cityId") String cityId);
}
