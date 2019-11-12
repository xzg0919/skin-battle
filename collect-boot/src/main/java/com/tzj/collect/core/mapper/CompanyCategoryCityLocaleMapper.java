package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCityLocale;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyCategoryCityLocaleMapper extends BaseMapper<CompanyCategoryCityLocale> {

    List<CategoryResult> getHouseHoldDetailLocale(@Param("parentId") String parentId, @Param("companyId") String companyId, @Param("cityId") String cityId);

    List<ComCatePrice> getTwoCategoryListLocale(@Param("categoryId") String categoryId, @Param("companyId") String companyId, @Param("cityId") String cityId);

    List<Category>  getOneCategoryListLocale(@Param("companyId")Integer companyId, @Param("cityId")Integer cityId);
}
