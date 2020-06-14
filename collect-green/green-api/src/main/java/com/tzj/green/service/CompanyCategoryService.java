package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.CompanyCategory;
import com.tzj.green.param.CategoryBean;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [公司分类关联表service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface CompanyCategoryService extends IService<CompanyCategory>
{


    Object getCompanyCategoryById(Long companyId);

    Object getAppCompanyCategoryById(Long companyId);

    Object updateCompanyCategoryPoints(Long companyId, CategoryBean categoryBean);
}