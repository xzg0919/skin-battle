package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.LogisticsCompanyCategory;
import com.tzj.collect.mapper.LogisticsCompanyCategoryMapper;
import com.tzj.collect.service.LogisticsCompanyCategoryService;
import org.springframework.beans.factory.annotation.Autowired;

public class LogisticsCompanyCategoryServiceImpl extends ServiceImpl<LogisticsCompanyCategoryMapper,LogisticsCompanyCategory> implements LogisticsCompanyCategoryService {

    @Autowired
    private LogisticsCompanyCategoryMapper logisticsCompanyCategoryMapper;




}