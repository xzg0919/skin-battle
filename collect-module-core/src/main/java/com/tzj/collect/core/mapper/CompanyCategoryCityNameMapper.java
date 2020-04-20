package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCityName;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface CompanyCategoryCityNameMapper extends BaseMapper<CompanyCategoryCityName> {

    List<Map<String, Object>> getCategoryListByCompanyCityId(@Param("companyId") String companyId, @Param("cityId") String cityId, @Param("categoryId") Integer categoryId);

    List<Category> getAppliceCategoryByCompanyId(@Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

    List<Category> getBigCategoryByCompanyId(@Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

    List<Category> getHouseCategoryByCompanyId(@Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

    List<Category> getHouseCategoryByCategoryId(@Param("categoryId") Integer categoryId, @Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

    List<Category> getFiveCategoryByCompanyId(@Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

    List<Category> getFiveCategoryByCategoryId(@Param("categoryId") Integer categoryId, @Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

    List<Category> getOneCategoryList(@Param("companyId") Integer companyId, @Param("cityId") Integer cityId, @Param("isCash") String isCash);

    List<Category> getTwoCategoryList(@Param("orderId")String orderId, @Param("categoryId") Integer categoryId, @Param("companyId") Integer companyId, @Param("cityId") Integer cityId, @Param("isCash") String isCash);

    List<Category> getTwoCategoryListLocal(@Param("categoryId") Integer categoryId, @Param("companyId") Integer companyId, @Param("cityId") Integer cityId, @Param("isCash") String isCash);
}
