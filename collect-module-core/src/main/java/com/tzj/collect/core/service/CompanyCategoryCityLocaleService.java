package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.entity.CompanyCategoryCityLocale;

import java.util.List;

public interface CompanyCategoryCityLocaleService extends IService<CompanyCategoryCityLocale> {

    @DS("slave")
    List<CategoryResult> getHouseHoldDetailLocale(String parentId, String companyId, String cityId);


}
