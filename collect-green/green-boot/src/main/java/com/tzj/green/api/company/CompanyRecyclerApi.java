package com.tzj.green.api.company;


import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Company;
import com.tzj.green.param.ProductBean;
import com.tzj.green.service.CompanyRecyclerService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class CompanyRecyclerApi {

    @Autowired
    private CompanyRecyclerService companyRecyclerService;


    /**
     * 获取盛情人员列表
     */
    @Api(name = "company.getApplyRecyclerList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getApplyRecyclerList() {
        Company company = CompanyUtils.getCompanyAccount();
        return  companyRecyclerService.getApplyRecyclerList(company.getId());
    }


}
