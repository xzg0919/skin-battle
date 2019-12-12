package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.result.business.BusinessCategoryResult;
import com.tzj.collect.core.param.ali.AliCategoryAttrOptionBean;
import com.tzj.collect.entity.CompanyCategoryAttrOptionCity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyCategoryAttrOptionCityMapper extends BaseMapper<CompanyCategoryAttrOptionCity> {


    List<BusinessCategoryResult> selectComCityCateAttOptPrice(@Param("cityid") String cityid, @Param("companyId") String companyId, @Param("categoryAttrId") String categoryAttrId);

    AliCategoryAttrOptionBean getCategoryAttrOptionByCityId(@Param("optionId") String optionId, @Param("companyId") String companyId, @Param("cityId") String cityId);

    CompanyCategoryAttrOptionCity getCategoryAttrOptionByCityCompanyId(@Param("optionId")String optionId, @Param("cityId")String cityId,  @Param("companyId") String companyId);

}
