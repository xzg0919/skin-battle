package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStree;

public interface CompanyStreeService extends IService<CompanyStree> {

        @DS("slave")
        Integer selectStreeCompanyIds(Integer categoryId, Integer streetId);


}
