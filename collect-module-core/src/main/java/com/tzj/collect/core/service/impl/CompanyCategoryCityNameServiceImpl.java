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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
        }else if ("9".equals(categoryBean.getTitle())){
            categoryList = categoryService.topList(0, 9,null);
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
    @Override
    public List<Category> getAppliceCategoryByCompanyId(Integer companyId, Integer cityId){
        return companyCategoryCityNameMapper.getAppliceCategoryByCompanyId(companyId,cityId);
    }


    @Override
    public List<Category> getBigCategoryByCompanyId(Integer companyId, Integer cityId){
        return companyCategoryCityNameMapper.getBigCategoryByCompanyId(companyId,cityId);
    }
    @Override
    public List<Category> getHouseCategoryByCompanyId(Integer houseceCompanyId,Integer cityId,Long parentId){
        List<Category> categoryList = companyCategoryCityNameMapper.getHouseCategoryByCompanyId(houseceCompanyId, cityId);
        if(null==parentId){
            categoryList.stream().forEach(category -> {
                List<Category> categoryList1 = companyCategoryCityNameMapper.getHouseCategoryByCategoryId(category.getId().intValue(),houseceCompanyId,cityId);
                category.setCategoryList(categoryList1);
            });
        }else {
            categoryList.stream().forEach(category -> {
                if (null!=parentId&&category.getId().equals(parentId)){
                    List<Category> categoryList1 = companyCategoryCityNameMapper.getHouseCategoryByCategoryId(category.getId().intValue(),houseceCompanyId,cityId);
                    category.setCategoryList(categoryList1);
                }
            });
        }
        List<Integer> delIndex =new ArrayList<>();
         for (int i=0 ;i<categoryList.size();i++){
             if(categoryList.get(i).getCategoryList()==null ||categoryList.get(i).getCategoryList().size() ==0 ){
                 delIndex.add(i);
             }
         }
        for (int i=0 ;i<delIndex.size();i++){
            int index = delIndex.get(i);
            categoryList.remove(index);
        }

        return categoryList;
    }
    @Override
    public List<Category> getFiveCategoryByCompanyId(Integer fiveCompanyId,Integer cityId){
        List<Category> categoryList = companyCategoryCityNameMapper.getFiveCategoryByCompanyId(fiveCompanyId, cityId);
        categoryList.stream().forEach(category -> {
            List<Category> categoryList1 = companyCategoryCityNameMapper.getFiveCategoryByCategoryId(category.getId().intValue(),fiveCompanyId,cityId);
            category.setCategoryList(categoryList1);
        });
        return categoryList;
    }
    @Override
    public List<Category> getOneCategoryList(Integer companyId,Integer cityId, String isCash){
        return companyCategoryCityNameMapper.getOneCategoryList(companyId,cityId,isCash);
    }
    @Override
    public List<Category> getTwoCategoryList(String orderId, Integer categoryId,Integer companyId,Integer cityId, String isCash){
        return companyCategoryCityNameMapper.getTwoCategoryList(orderId, categoryId,companyId,cityId,isCash);
    }
    @Override
    public List<Category> getTwoCategoryListLocal(Integer categoryId,Integer companyId,Integer cityId, String isCash){
        return companyCategoryCityNameMapper.getTwoCategoryListLocal(categoryId,companyId,cityId,isCash);
    }

    @Override
    public boolean isAbleCategory(Integer companyId, Integer streetId, int parentId) {
        Category streetCategory = categoryService.selectById(streetId);
        Category districtCategory = categoryService.selectById(streetCategory.getParentId());
        Integer count = companyCategoryCityNameMapper.selectCount(new EntityWrapper<CompanyCategoryCityName>().eq("city_id", districtCategory.getParentId()).eq("parent_id", parentId).eq("company_id", companyId).eq("del_flag", 0));
        if(count!= null && count !=0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<Category> getElectroMobileCategoryByCompanyId(Integer companyId, Integer cityId) {
        return companyCategoryCityNameMapper.getElectroMobileCategoryByCompanyId(companyId,cityId);
    }
}
