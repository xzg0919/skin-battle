package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CategoryCity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryCityMapper extends BaseMapper<CategoryCity> {


    List<Map<String,Object>> getCategoryCityLists(@Param("cityId") String cityId, @Param("categoryId")Integer categoryId);

    List<Map<String,Object>> getCategoryAttrOptionCityLists(@Param("cityId") String cityId,@Param("categoryAttrId")Integer categoryAttrId);

}
