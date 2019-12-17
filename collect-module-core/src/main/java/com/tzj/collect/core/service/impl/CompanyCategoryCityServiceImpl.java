package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyCategoryCityMapper;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class CompanyCategoryCityServiceImpl extends ServiceImpl<CompanyCategoryCityMapper, CompanyCategoryCity> implements CompanyCategoryCityService {
    @Autowired
    private CompanyCategoryCityMapper companyCategoryCityMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CompanyCategoryCityNameService companyCategoryCityNameService;
    @Autowired
    private CompanyCategoryService companyCategoryService;
    @Autowired
    private CategoryAttrService categoryAttrService;
    @Autowired
    private CompanyCategoryAttrOptionCityService companyCategoryAttrOptionCityService;


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
    @Override
    public Map<String,Object> getCompanyCategoryListByCityTitle(CategoryBean categoryBean){
        Integer count = companyCategoryCityMapper.selectCategoryCount(categoryBean.getTitle(), categoryBean.getCompanyId());
        if (count<1){
            throw new ApiException("所在公司暂不回收该分类");
        }
        List<Category> categories = categoryService.selectList(new EntityWrapper<Category>().eq("title", categoryBean.getTitle()).eq("level_", "0").orderBy("code_",true));
        categories.stream().forEach(category -> {
            List<Map<String, Object>> areaCityRatioLists = companyCategoryCityMapper.getCompanyCategoryListByCityTitle(categoryBean.getCompanyId(), Integer.parseInt(categoryBean.getCityId()), category.getId().intValue());
            category.setCategoryMap(areaCityRatioLists);
        });
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("categoryList",categories);
        return resultMap;
    }
    @Override
    public Map<String,Object> getCompanyCategoryAttrOptionCityLists(CategoryBean categoryBean){
        List<CategoryAttr> CategoryAttrList = categoryAttrService.selectList(new EntityWrapper<CategoryAttr>().eq("category_id", categoryBean.getCategoryId()));
        CategoryAttrList.stream().forEach(categoryAttr -> {
            List<Map<String, Object>> categoryAttrOptionCityLists = companyCategoryCityMapper.getCompanyCategoryAttrOptionCityLists(categoryBean.getCompanyId(),Integer.parseInt(categoryBean.getCityId()), categoryAttr.getId().intValue());
            categoryAttr.setObjectMap(categoryAttrOptionCityLists);
        });
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("CategoryAttrList",CategoryAttrList);
        return resultMap;
    }
    @Override
    @Transactional
    public String updateCompanyCategoryPriceByCategoryId(CategoryBean categoryBean){

        if (StringUtils.isNotBlank(categoryBean.getAdminCommissions())||StringUtils.isNotBlank(categoryBean.getCompanyCommissions())){
            CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id", categoryBean.getCompanyId()).eq("category_id", categoryBean.getCategoryId()));
            if (StringUtils.isNotBlank(categoryBean.getAdminCommissions())){
                companyCategory.setAdminCommissions(new BigDecimal(categoryBean.getAdminCommissions()));
                companyCategory.setIsCommissions("1");
            }
            if (StringUtils.isNotBlank(categoryBean.getCompanyCommissions())){
                companyCategory.setCompanyCommissions(new BigDecimal(categoryBean.getCompanyCommissions()));
                companyCategory.setIsCommissions("1");
            }
            if (StringUtils.isNotBlank(categoryBean.getFreeCommissions())){
                companyCategory.setFreeCommissions(new BigDecimal(categoryBean.getFreeCommissions()));
                companyCategory.setIsCommissions("1");
            }
            companyCategoryService.updateById(companyCategory);
        }
        if (StringUtils.isNotBlank(categoryBean.getPrice())){
            CompanyCategoryCity companyCategoryCity = this.selectOne(new EntityWrapper<CompanyCategoryCity>().eq("city_id", categoryBean.getCityId()).eq("company_id",categoryBean.getCompanyId()).eq("category_id", categoryBean.getCategoryId()));
            if (null == companyCategoryCity){
                companyCategoryCity = new CompanyCategoryCity();
                Category category = categoryService.selectById(categoryBean.getCategoryId());
                Category parentCategory = categoryService.selectById(category.getParentId());
                companyCategoryCity.setCityId(categoryBean.getCityId());
                companyCategoryCity.setCategoryId(category.getId().intValue());
                companyCategoryCity.setParentId(category.getParentId());
                companyCategoryCity.setParentName(parentCategory.getName());
                companyCategoryCity.setParentIds(category.getParentIds());
                companyCategoryCity.setCompanyId(categoryBean.getCompanyId().toString());
                companyCategoryCity.setUnit(category.getUnit());
            }
            companyCategoryCity.setPrice(new BigDecimal(categoryBean.getPrice()));
            this.insertOrUpdate(companyCategoryCity);
        }
        return "操作成功";
    }
    @Override
    @Transactional
    public String updateCompanyCategoryAttrOptionByOptionId(CategoryBean categoryBean){
        CompanyCategoryAttrOptionCity companyCategoryAttrOptionCity = companyCategoryAttrOptionCityService.selectOne(new EntityWrapper<CompanyCategoryAttrOptionCity>().eq("city_id", categoryBean.getCityId()).eq("category_attr_option_id", categoryBean.getAttrOptionId()).eq("company_id",categoryBean.getCompanyId()));
        if (null == companyCategoryAttrOptionCity){
            companyCategoryAttrOptionCity = new CompanyCategoryAttrOptionCity();
            companyCategoryAttrOptionCity.setCityId(categoryBean.getCityId());
            companyCategoryAttrOptionCity.setCompanyId(categoryBean.getCompanyId().toString());
            companyCategoryAttrOptionCity.setCategoryAttrOptionId(Integer.parseInt(categoryBean.getAttrOptionId()));
        }
        if (StringUtils.isNotBlank(categoryBean.getIsRecovery())){
            companyCategoryAttrOptionCity.setIsRecovery(categoryBean.getIsRecovery());
        }
        if (StringUtils.isNotBlank(categoryBean.getIsSpecial())){
            companyCategoryAttrOptionCity.setIsSpecial(categoryBean.getIsSpecial());
        }
        if (StringUtils.isNotBlank(categoryBean.getPrice())){
            companyCategoryAttrOptionCity.setAttrOptionPrice(new BigDecimal(categoryBean.getPrice()));
        }
        if (StringUtils.isNotBlank(categoryBean.getSpecialPrice())){
            companyCategoryAttrOptionCity.setSpecialPrice(new BigDecimal(categoryBean.getSpecialPrice()));
        }
        companyCategoryAttrOptionCityService.insertOrUpdate(companyCategoryAttrOptionCity);
        return "操作成功";
    }

}
