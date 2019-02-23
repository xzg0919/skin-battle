package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.LogisticsCompany;
import com.tzj.collect.mapper.LogisticsCompanyMapper;
import com.tzj.collect.service.LogisticsCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class LogisticsCompanyServiceImpl extends ServiceImpl<LogisticsCompanyMapper,LogisticsCompany> implements LogisticsCompanyService{

    @Autowired
    private LogisticsCompanyMapper logisticsCompanyMapper;

    @Override
    public Integer selectLogisticeCompanyIds(Integer categoryId, Integer streeId) {

        return logisticsCompanyMapper.selectLogisticeCompanyIds(categoryId,streeId);
    }
}
