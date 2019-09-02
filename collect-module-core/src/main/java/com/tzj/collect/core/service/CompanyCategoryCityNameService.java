package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.admin.CategoryBean;
import com.tzj.collect.entity.CompanyCategoryCityName;

public interface CompanyCategoryCityNameService extends IService<CompanyCategoryCityName> {

    @DS("slave")
    Object getCategoryListByCompanyCityId(CategoryBean categoryBean);

    Object saveOrDeleteCategoryById(CategoryBean categoryBean);
}
