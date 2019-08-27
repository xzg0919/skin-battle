package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyCategoryCityMapper;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.core.service.CompanyCategoryCityService;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class CompanyCategoryCityServiceImpl extends ServiceImpl<CompanyCategoryCityMapper, CompanyCategoryCity> implements CompanyCategoryCityService {
    @Autowired
    private CompanyCategoryCityMapper companyCategoryCityMapper;


    @Override
    public List<CategoryResult> getCityHouseHoldDetail(String parentId, String companyId, String cityId) {
        return companyCategoryCityMapper.getCityHouseHoldDetail(parentId,companyId,cityId);
    }

    @Override
    public List<ComCatePrice> getOwnnerPriceBycityId(String categoryId, String companyId, String cityId) {
        return companyCategoryCityMapper.getOwnnerPriceBycityId(categoryId,companyId,cityId);
    }

    @Override
    public List<ComCatePrice> getOwnnerNoPriceBycityId(String categoryId, String companyId, String cityId) {
        return companyCategoryCityMapper.getOwnnerNoPriceBycityId(categoryId,companyId,cityId);
    }

    @Override
    public List<Category> topListAppByCity(String level, String title, String companyId, String cityId) {
        return companyCategoryCityMapper.topListAppByCity(level,title,companyId,cityId);
    }

    @Override
    public List<ComCatePrice> getOwnnerPriceAppByCity(String categoryId, String companyId, String cityId) {
        return companyCategoryCityMapper.getOwnnerPriceAppByCity(categoryId,companyId,cityId);
    }

    @Override
    public List<Category> getOneCategoryListByOrder(Integer companyId, Integer cityId){
        return companyCategoryCityMapper.getOneCategoryListByOrder(companyId,cityId);
    }
}
