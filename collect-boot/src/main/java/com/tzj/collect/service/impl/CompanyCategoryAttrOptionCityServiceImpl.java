package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.ali.param.AliCategoryAttrOptionBean;
import com.tzj.collect.api.business.result.BusinessCategoryResult;
import com.tzj.collect.entity.CompanyCategoryAttrOptionCity;
import com.tzj.collect.mapper.CompanyCategoryAttrOptionCityMapper;
import com.tzj.collect.service.CompanyCategoryAttrOptionCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class CompanyCategoryAttrOptionCityServiceImpl extends ServiceImpl<CompanyCategoryAttrOptionCityMapper, CompanyCategoryAttrOptionCity> implements CompanyCategoryAttrOptionCityService {
    @Autowired
    private CompanyCategoryAttrOptionCityMapper companyCategoryAttrOptionCityMapper;


    @Override
    public List<BusinessCategoryResult> selectComCityCateAttOptPrice(String cityid, String companyId, String categoryAttrId) {
        return companyCategoryAttrOptionCityMapper.selectComCityCateAttOptPrice(cityid,companyId,categoryAttrId);
    }

    @Override
    public AliCategoryAttrOptionBean getCategoryAttrOptionByCityId(String optionId, String companyId, String cityId) {
        return companyCategoryAttrOptionCityMapper.getCategoryAttrOptionByCityId(optionId,companyId,cityId);
    }
}
