package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyCategoryCityMapper;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.core.service.CategoryAttrOptionService;
import com.tzj.collect.core.service.CategoryService;
import com.tzj.collect.core.service.CompanyCategoryCityNameService;
import com.tzj.collect.core.service.CompanyCategoryCityService;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CategoryAttrOption;
import com.tzj.collect.entity.CompanyCategoryCity;
import com.tzj.collect.entity.CompanyCategoryCityName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class CompanyCategoryCityServiceImpl extends ServiceImpl<CompanyCategoryCityMapper, CompanyCategoryCity> implements CompanyCategoryCityService {
    @Autowired
    private CompanyCategoryCityMapper companyCategoryCityMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CompanyCategoryCityNameService companyCategoryCityNameService;


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
    @Transactional
    @Override
    public boolean updateCompanyAreaCategoryRange(Integer companyId, Integer cityId,String title){
        List<Category> categoryList = null;
        if ("1".equals(title)){
            categoryList = categoryService.selectList(new EntityWrapper<Category>().eq("title", 1).eq("level_", 1));
        }else if("2".equals(title)){
            categoryList = categoryService.selectList(new EntityWrapper<Category>().eq("title", 2).eq("level_", 1));
        }
        else if("4".equals(title)){
            categoryList = categoryService.selectList(new EntityWrapper<Category>().eq("title", 4).eq("level_", 1));
        }
        categoryList.stream().forEach(category -> {
            CompanyCategoryCityName companyCategoryCityName = companyCategoryCityNameService.selectOne(new EntityWrapper<CompanyCategoryCityName>().eq("company_id", companyId).eq("city_id", cityId).eq("category_id", category.getId()));
            if (null == companyCategoryCityName){
                companyCategoryCityName = new CompanyCategoryCityName();
                Category parentCategory = categoryService.selectById(category.getParentId());
                companyCategoryCityName.setCompanyId(companyId.toString());
                companyCategoryCityName.setCityId(cityId.toString());
                companyCategoryCityName.setCategoryId(category.getId().intValue());
                companyCategoryCityName.setParentId(category.getParentId());
                companyCategoryCityName.setParentName(parentCategory.getName());
                companyCategoryCityName.setParentIds(category.getParentIds());
                companyCategoryCityName.setPrice(category.getMarketPrice());
                companyCategoryCityName.setUnit(category.getUnit());
                companyCategoryCityNameService.insert(companyCategoryCityName);
            }
        });
        return true;
    }
}
