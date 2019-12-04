package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.admin.CategoryBean;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCityName;

import java.util.List;

public interface CompanyCategoryCityNameService extends IService<CompanyCategoryCityName> {

    @DS("slave")
    Object getCategoryListByCompanyCityId(CategoryBean categoryBean);

    Object saveOrDeleteCategoryById(CategoryBean categoryBean);
    @DS("slave")
    List<Category> getAppliceCategoryByCompanyId(Integer companyId, Integer cityId);
    @DS("slave")
    List<Category> getBigCategoryByCompanyId(Integer companyId, Integer cityId);
    @DS("slave")
    List<Category> getHouseCategoryByCompanyId(Integer houseceCompanyId,Integer cityId);
    @DS("slave")
    List<Category> getFiveCategoryByCompanyId(Integer fiveCompanyId,Integer cityId);
    @DS("slave")
    List<Category> getOneCategoryList(Integer companyId,Integer cityId, String isCash);
    @DS("slave")
    List<Category> getTwoCategoryList(Integer categoryId,Integer companyId,Integer cityId, String isCash);
    @DS("slave")
    List<Category> getTwoCategoryListLocal(Integer categoryId,Integer companyId,Integer cityId, String isCash);
}
