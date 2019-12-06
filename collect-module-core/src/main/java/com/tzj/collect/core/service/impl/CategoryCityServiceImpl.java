package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CategoryCityMapper;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryCityServiceImpl extends ServiceImpl<CategoryCityMapper, CategoryCity> implements CategoryCityService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryCityMapper categoryCityMapper;
    @Autowired
    private CategoryAttrService categoryAttrService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CategoryAttrOptionCityService categoryAttrOptionCityService;


    @Override
    public Map<String, Object> getCategoryCityLists(CategoryBean categoryBean) {
        List<Category> categories = categoryService.selectList(new EntityWrapper<Category>().eq("title", categoryBean.getTitle()).eq("level_", 0).orderBy("code_",true));
        categories.stream().forEach(category -> {
            List<Map<String, Object>> categoryCityLists = categoryCityMapper.getCategoryCityLists(categoryBean.getCityId(), category.getId().intValue());
            category.setCategoryMap(categoryCityLists);
        });
        Area area = areaService.selectById(categoryBean.getCityId());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("categoryList",categories);
        resultMap.put("area",area);
        return resultMap;
    }
    @Override
    public Map<String, Object> getCategoryAttrOptionCityLists(CategoryBean categoryBean) {

        List<CategoryAttr> CategoryAttrList = categoryAttrService.selectList(new EntityWrapper<CategoryAttr>().eq("category_id", categoryBean.getCategoryId()));
        CategoryAttrList.stream().forEach(categoryAttr -> {
            List<Map<String, Object>> categoryAttrOptionCityLists = categoryCityMapper.getCategoryAttrOptionCityLists(categoryBean.getCityId(), categoryAttr.getId().intValue());
            categoryAttr.setObjectMap(categoryAttrOptionCityLists);
        });
        Area area = areaService.selectById(categoryBean.getCityId());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("area",area);
        resultMap.put("CategoryAttrList",CategoryAttrList);
        return resultMap;
    }
    @Override
    @Transactional
    public String updateCategoryPriceByCategoryId(CategoryBean categoryBean){
        CategoryCity categoryCity = this.selectOne(new EntityWrapper<CategoryCity>().eq("city_id", categoryBean.getCityId()).eq("category_id", categoryBean.getCategoryId()));
        if (null == categoryCity){
            categoryCity = new CategoryCity();
            Category category = categoryService.selectById(categoryBean.getCategoryId());
            Category parentCategory = categoryService.selectById(category.getParentId());
            categoryCity.setCityId(categoryBean.getCityId());
            categoryCity.setCategoryId(category.getId().intValue());
            categoryCity.setParentId(category.getParentId());
            categoryCity.setParentName(parentCategory.getName());
            categoryCity.setParentIds(category.getParentIds());
            categoryCity.setUnit(category.getUnit());
        }
        categoryCity.setPrice(new BigDecimal(categoryBean.getPrice()));
        this.insertOrUpdate(categoryCity);
        return "操作成功";
    }
    @Override
    @Transactional
    public String updateCategoryAttrOptionByOptionId(CategoryBean categoryBean){
        CategoryAttrOptionCity categoryAttrOptionCity = categoryAttrOptionCityService.selectOne(new EntityWrapper<CategoryAttrOptionCity>().eq("city_id", categoryBean.getCityId()).eq("category_attr_option_id", categoryBean.getAttrOptionId()));
        if (null == categoryAttrOptionCity){
            categoryAttrOptionCity = new CategoryAttrOptionCity();
            categoryAttrOptionCity.setCityId(categoryBean.getCityId());
            categoryAttrOptionCity.setCategoryAttrOptionId(Integer.parseInt(categoryBean.getAttrOptionId()));
        }
        if (StringUtils.isNotBlank(categoryBean.getIsRecovery())){
            categoryAttrOptionCity.setIsRecovery(categoryBean.getIsRecovery());
        }
        if (StringUtils.isNotBlank(categoryBean.getIsSpecial())){
            categoryAttrOptionCity.setIsSpecial(categoryBean.getIsSpecial());
        }
        if (StringUtils.isNotBlank(categoryBean.getPrice())){
            categoryAttrOptionCity.setAttrOptionPrice(new BigDecimal(categoryBean.getPrice()));
        }
        if (StringUtils.isNotBlank(categoryBean.getSpecialPrice())){
            categoryAttrOptionCity.setSpecialPrice(new BigDecimal(categoryBean.getSpecialPrice()));
        }
        categoryAttrOptionCityService.insertOrUpdate(categoryAttrOptionCity);
        return "操作成功";
    }
}
