package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStree;
import com.tzj.collect.entity.CompanyStreetAppSmall;

public interface CompanyStreetAppSmallService extends IService<CompanyStreetAppSmall> {

        @DS("slave")
        Integer selectStreetAppSmallCompanyIds(Integer cityId, Integer streetId);


}
