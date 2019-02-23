package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.LogisticsCompany;

public interface LogisticsCompanyService extends IService<LogisticsCompany> {

    Integer selectLogisticeCompanyIds(Integer categoryId,Integer streeId);

}
