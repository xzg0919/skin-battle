package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.AliCategoryAttrOptionBean;
import com.tzj.collect.api.business.result.BusinessCategoryResult;
import com.tzj.collect.entity.CompanyCategoryAttrOptionCity;

import java.util.List;

public interface CompanyCategoryAttrOptionCityService extends IService<CompanyCategoryAttrOptionCity> {

    @DS("slave")
    List<BusinessCategoryResult> selectComCityCateAttOptPrice(String cityid, String companyId, String categoryAttrId);
    @DS("slave")
    AliCategoryAttrOptionBean getCategoryAttrOptionByCityId(String optionId, String companyId, String cityId);

}
