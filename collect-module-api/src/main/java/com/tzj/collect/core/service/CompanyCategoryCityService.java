package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCity;

import java.util.List;
import java.util.Map;

public interface CompanyCategoryCityService extends IService<CompanyCategoryCity> {
    @DS("slave")
    List<CategoryResult> getCityHouseHoldDetail(String parentId, String companyId, String cityId);

    @DS("slave")
    List<ComCatePrice> getOwnnerPriceBycityId(String categoryId, String companyId, String cityId);
    @DS("slave")
    List<ComCatePrice> getOwnnerNoPriceBycityId(String categoryId, String companyId, String cityId);
    @DS("slave")
    List<Category> topListAppByCity(String level, String title, String companyId, String cityId);
    @DS("slave")
    List<ComCatePrice> getOwnnerPriceAppByCity(String categoryId, String companyId, String cityId);
    @DS("slave")
    List<Category> getOneCategoryListByOrder(Integer companyId, Integer cityId);

    boolean updateCompanyAreaCategoryRange(Integer companyId, Integer cityId,String title);
    @DS("slave")
    Map<String,Object> getCompanyCategoryListByCityTitle(CategoryBean categoryBean);
    @DS("slave")
    Map<String,Object> getCompanyCategoryAttrOptionCityLists(CategoryBean categoryBean);

    String updateCompanyCategoryPriceByCategoryId(CategoryBean categoryBean);

    String updateCompanyCategoryAttrOptionByOptionId(CategoryBean categoryBean);

}
