package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStreetBig;

public interface CompanyStreetBigService  extends IService<CompanyStreetBig> {

    Integer selectStreetBigCompanyId(Integer categoryId, Integer streetId);
}
