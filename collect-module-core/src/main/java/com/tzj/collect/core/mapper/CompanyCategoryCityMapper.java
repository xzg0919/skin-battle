package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyCategoryCityMapper extends BaseMapper<CompanyCategoryCity> {

    List<CategoryResult> getCityHouseHoldDetail(@Param("parentId") String parentId, @Param("companyId") String companyId, @Param("cityId") String cityId);

    List<ComCatePrice> getOwnnerPriceBycityId(@Param("categoryId") String categoryId, @Param("companyId") String companyId, @Param("cityId") String cityId);

    List<ComCatePrice> getOwnnerNoPriceBycityId(@Param("categoryId") String categoryId, @Param("companyId") String companyId, @Param("cityId") String cityId);

    List<Category> topListAppByCity(@Param("level") String level, @Param("title") String title, @Param("companyId") String companyId, @Param("cityId") String cityId);

    List<ComCatePrice> getOwnnerPriceAppByCity(@Param("categoryId") String categoryId, @Param("companyId") String companyId, @Param("cityId") String cityId);

    List<Category>  getOneCategoryListByOrder(@Param("companyId")Integer companyId,@Param("cityId")Integer cityId);

    List<Map<String,Object>> getCompanyCategoryListByCityTitle(@Param("companyId")Integer companyId,@Param("cityId") Integer cityId,@Param("categoryId")Integer categoryId);

    List<Map<String,Object>> getCompanyCategoryAttrOptionCityLists(@Param("companyId")Integer companyId,@Param("cityId") Integer cityId, @Param("categoryAttrId") Integer categoryAttrId);

    Integer selectCategoryCount(@Param("title")String title,@Param("companyId")Integer companyId);
}
