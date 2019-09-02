package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyCategoryCityName;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyCategoryCityNameMapper extends BaseMapper<CompanyCategoryCityName> {

    List<Map<String,Object>> getCategoryListByCompanyCityId(@Param("companyId") String companyId,@Param("cityId") String cityId, @Param("categoryId") Integer categoryId);

}
