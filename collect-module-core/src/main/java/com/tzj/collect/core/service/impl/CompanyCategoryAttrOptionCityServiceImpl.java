package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.result.business.BusinessCategoryResult;
import com.tzj.collect.core.mapper.CompanyCategoryAttrOptionCityMapper;
import com.tzj.collect.core.param.ali.AliCategoryAttrOptionBean;
import com.tzj.collect.core.service.CompanyCategoryAttrOptionCityService;
import com.tzj.collect.entity.CompanyCategoryAttrOptionCity;
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
    @Override
    public CompanyCategoryAttrOptionCity getCategoryAttrOptionByCityCompanyId(String optionId, String cityId, String companyId){
        return companyCategoryAttrOptionCityMapper.getCategoryAttrOptionByCityCompanyId(optionId,cityId,companyId);
    }
}
