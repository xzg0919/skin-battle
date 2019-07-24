package com.tzj.collect.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.CompanyStreetBig;
import com.tzj.collect.mapper.CompanyStreeMapper;
import com.tzj.collect.mapper.CompanyStreetBigMapper;
import com.tzj.collect.service.CompanyStreetBigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly=true)
public class CompanyStreetBigServiceImpl extends ServiceImpl<CompanyStreetBigMapper, CompanyStreetBig> implements CompanyStreetBigService {

    @Autowired
    private CompanyStreetBigMapper companyStreetBigMapper;

    @Override
    public Integer selectStreetBigCompanyId(Integer categoryId, Integer streetId) {


        return companyStreetBigMapper.selectStreetBigCompanyId(categoryId,streetId);
    }

    @Override
    public Map<String,Object> companyAreaRanges(String companyId) {
        return companyStreetBigMapper.companyAreaRanges(companyId);
    }
    @Override
    public Object getAreaStreetList(long companyId, String cityName, String areaName, Integer starts, Integer ends) {
        return companyStreetBigMapper.getAreaStreetList(companyId,cityName,areaName,starts,ends);
    }

    @Override
    public Object getAreaStreetCount(long companyId, String cityName, String areaName) {
        return companyStreetBigMapper.getAreaStreetCount(companyId,cityName,areaName);
    }
}
