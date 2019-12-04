package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CategoryCityMapper;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.service.CategoryCityService;
import com.tzj.collect.core.service.CategoryService;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CategoryCity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryCityServiceImpl extends ServiceImpl<CategoryCityMapper, CategoryCity> implements CategoryCityService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryCityMapper categoryCityMapper;


    @Override
    public Map<String, Object> getCategoryCityLists(CategoryBean categoryBean) {
        List<Category> categories = categoryService.selectList(new EntityWrapper<Category>().eq("title", categoryBean.getTitle()).eq("level_", 0));
        categories.stream().forEach(category -> {
            //categoryCityMapper.getCategoryCityLists(categoryBean.getCityId(),category.getCategoryId());
        });

        return null;
    }
}
