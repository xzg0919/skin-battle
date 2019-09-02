package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyCategoryCityNameMapper;
import com.tzj.collect.core.param.admin.CategoryBean;
import com.tzj.collect.core.service.CategoryService;
import com.tzj.collect.core.service.CompanyCategoryCityNameService;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCityName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class CompanyCategoryCityNameServiceImpl extends ServiceImpl<CompanyCategoryCityNameMapper, CompanyCategoryCityName> implements CompanyCategoryCityNameService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CompanyCategoryCityNameMapper companyCategoryCityNameMapper;

    @Override
    public Object getCategoryListByCompanyCityId(CategoryBean categoryBean) {
        List<Category> categoryList = null;
        if ("1".equals(categoryBean.getTitle())){
            categoryList = categoryService.topList(0, 1,null);
        }else if ("2".equals(categoryBean.getTitle())){
            categoryList = categoryService.topList(0, 2,null);
        }else if ("4".equals(categoryBean.getTitle())){
            categoryList = categoryService.topList(0, 4,null);
        }
        if (null != categoryList ){
            categoryList.stream().forEach(category -> {
                List<Map<String, Object>> categoryListByCompanyCityId = companyCategoryCityNameMapper.getCategoryListByCompanyCityId(categoryBean.getCompanyId(), categoryBean.getCityId(), category.getId().intValue());
                category.setCategoryMap(categoryListByCompanyCityId);
            });
        }
        return categoryList;
    }

    @Override
    @Transactional
    public Object saveOrDeleteCategoryById(CategoryBean categoryBean) {
        List<String> paramList = categoryBean.getParamList();
        if (null != paramList){
            paramList.stream().forEach(s -> {
                if ("0".equals(categoryBean.getIsOpen())){
                    CompanyCategoryCityName companyCategoryCityName = this.selectOne(new EntityWrapper<CompanyCategoryCityName>().eq("company_id", categoryBean.getCompanyId()).eq("city_id", categoryBean.getCityId()).eq("category_id", s));
                    if (null == companyCategoryCityName){
                        companyCategoryCityName = new CompanyCategoryCityName();
                        Category category = categoryService.selectById(s);
                        Category parentCategory = categoryService.selectById(category.getParentId());
                        companyCategoryCityName.setCompanyId(categoryBean.getCompanyId());
                        companyCategoryCityName.setCityId(categoryBean.getCityId());
                        companyCategoryCityName.setCategoryId(category.getId().intValue());
                        companyCategoryCityName.setParentId(category.getParentId());
                        companyCategoryCityName.setParentName(parentCategory.getName());
                        companyCategoryCityName.setParentIds(category.getParentIds());
                        companyCategoryCityName.setPrice(category.getMarketPrice());
                        companyCategoryCityName.setUnit(category.getUnit());
                        this.insert(companyCategoryCityName);
                    }
                }else {
                    this.delete(new EntityWrapper<CompanyCategoryCityName>().eq("company_id",categoryBean.getCompanyId()).eq("city_id",categoryBean.getCityId()).eq("category_id",s));
                }
            });
        }
        return "操作成功";
    }


}
