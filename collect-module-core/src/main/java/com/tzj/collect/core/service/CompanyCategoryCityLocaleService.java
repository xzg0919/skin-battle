package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCityLocale;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyCategoryCityLocaleService extends IService<CompanyCategoryCityLocale> {

    @DS("slave")
    List<CategoryResult> getHouseHoldDetailLocale(String parentId, String companyId, String cityId);
    @DS("slave")
    List<ComCatePrice> getTwoCategoryListLocale(String categoryId,String companyId,String cityId);
    @DS("slave")
    List<Category>  getOneCategoryListLocale(Integer companyId,Integer cityId);
}
