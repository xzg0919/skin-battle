package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;

import com.tzj.green.entity.Category;
import com.tzj.green.entity.CompanyCategory;
import com.tzj.green.mapper.CompanyCategoryMapper;
import com.tzj.green.param.CategoryBean;
import com.tzj.green.service.CategoryService;
import com.tzj.green.service.CompanyCategoryService;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [公司分类关联表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CompanyCategoryServiceImpl extends ServiceImpl<CompanyCategoryMapper, CompanyCategory> implements CompanyCategoryService
{
    @Resource
    private CompanyCategoryMapper companyCategoryMapper;
    @Resource
    private CategoryService categoryService;

    @Override
    public Object getCompanyCategoryById(Long companyId) {

        List<Map<String, Object>> companyCategorys = companyCategoryMapper.getCompanyCategoryById(companyId, null,"0");
        companyCategorys.stream().forEach(map -> {
                List<Map<String, Object>> companyCategoryTs = companyCategoryMapper.getCompanyCategoryById(companyId, map.get("id").toString(), "1");
                map.put("companyCategoryTs",companyCategoryTs);
                companyCategoryTs.stream().forEach(maps -> {
                        List<Map<String, Object>> companyCategoryThree = companyCategoryMapper.getCompanyCategoryById(companyId, maps.get("id").toString(), "2");
                        maps.put("companyCategoryThree",companyCategoryThree);
                });
        });
        return companyCategorys;
    }

    @Override
    @Transactional
    public Object updateCompanyCategoryPoints(Long companyId, CategoryBean categoryBean) {

        Category category = categoryService.selectById(categoryBean.getCategoryId());
        CompanyCategory companyCategory = this.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id", companyId).eq("category_id", categoryBean.getCategoryId()));
        if (null == companyCategory){
            companyCategory = new CompanyCategory();
        }
        companyCategory.setCompanyId(companyId);
        companyCategory.setCategoryId(category.getId());
        companyCategory.setCategoryName(category.getName());
        if (StringUtils.isNotBlank(categoryBean.getAddPoints())){
            companyCategory.setAddPoints(Long.parseLong(categoryBean.getAddPoints()));
        }
        if (StringUtils.isNotBlank(categoryBean.getSubtractPoints())){
            companyCategory.setSubtractPoints(Long.parseLong(categoryBean.getSubtractPoints()));
        }
        this.insertOrUpdate(companyCategory);
        return "操作成功";
    }
}