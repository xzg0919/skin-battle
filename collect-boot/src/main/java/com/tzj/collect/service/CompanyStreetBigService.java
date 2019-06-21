package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStreetBig;

public interface CompanyStreetBigService  extends IService<CompanyStreetBig> {
    @DS("slave")
    Integer selectStreetBigCompanyId(Integer categoryId, Integer streetId);
}
