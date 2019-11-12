package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyStreetBigMapper;
import com.tzj.collect.core.service.CompanyStreetBigService;
import com.tzj.collect.entity.CompanyStreetBig;
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
    public Integer selectStreetBigCompanyId(Integer streetId) {
        return companyStreetBigMapper.selectStreetBigCompanyId(streetId);
    }
    @Override
    public Integer selectStreetBigCompanyIdByCategoryId(Integer categoryId,Integer streetId) {
        return companyStreetBigMapper.selectStreetBigCompanyIdByCategoryId(categoryId,streetId);
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
