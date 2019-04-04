package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyStree;

public interface CompanyStreeService extends IService<CompanyStree> {


        Integer selectStreeCompanyIds(Integer categoryId, Integer streetId);


}
