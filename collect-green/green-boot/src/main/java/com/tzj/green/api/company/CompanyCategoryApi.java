package com.tzj.green.api.company;


import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Company;
import com.tzj.green.param.CategoryBean;
import com.tzj.green.param.PointsListBean;
import com.tzj.green.service.CompanyCategoryService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class CompanyCategoryApi {

    @Resource
    private CompanyCategoryService companyCategoryService;

    /**
     * 积分流水查询
     */
    @Api(name = "company.getCompanyCategoryById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getCompanyCategoryById() {
        Company company = CompanyUtils.getCompanyAccount();
        return companyCategoryService.getCompanyCategoryById(company.getId());
    }

    /**
     * 积分流水查询
     */
    @Api(name = "company.updateCompanyCategoryPoints", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object updateCompanyCategoryPoints(CategoryBean categoryBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return companyCategoryService.updateCompanyCategoryPoints(company.getId(),categoryBean);
    }

}
