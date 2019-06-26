package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.result.ComCatePrice;
import com.tzj.collect.api.business.result.CategoryResult;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCity;

import java.util.List;

public interface CompanyCategoryCityService extends IService<CompanyCategoryCity> {
    @DS("slave")
    List<CategoryResult> getCityHouseHoldDetail(String parentId, String companyId,String cityId);

    @DS("slave")
    List<ComCatePrice> getOwnnerPriceBycityId(String categoryId,String companyId,String cityId);
    @DS("slave")
    List<ComCatePrice> getOwnnerNoPriceBycityId(String categoryId,String companyId,String cityId);
    @DS("slave")
    List<Category> topListAppByCity(String level,String title,String companyId,String cityId);
    @DS("slave")
    List<ComCatePrice> getOwnnerPriceAppByCity(String categoryId,String companyId,String cityId);

}
