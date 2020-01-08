package com.tzj.green.api.company;


import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Company;
import com.tzj.green.param.ProductBean;
import com.tzj.green.param.RecyclerBean;
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
     * 获取申请人员列表
     */
    @Api(name = "company.getApplyRecyclerList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getApplyRecyclerList() {
        Company company = CompanyUtils.getCompanyAccount();
        return  companyRecyclerService.getApplyRecyclerList(company.getId());
    }


    /**
     * 同意或拒绝回收人员的申请
     */
    @Api(name = "company.updateApplyRecyclerStatus", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object updateApplyRecyclerStatus(RecyclerBean recyclerBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return  companyRecyclerService.updateApplyRecyclerStatus(recyclerBean,company.getId());
    }

    /**
     * 获取该公司的回收人员列表
     */
    @Api(name = "company.getCompanyRecyclerList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getCompanyRecyclerList(RecyclerBean recyclerBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return  companyRecyclerService.getCompanyRecyclerList(recyclerBean,company.getId());
    }

    /**
     * 根据回收人员Id获取详细信息
     */
    @Api(name = "company.getRecyclerDetailById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getRecyclerDetailById(RecyclerBean recyclerBean) {
        return  companyRecyclerService.getRecyclerDetailById(recyclerBean.getId());
    }

    /**
     * 删除回收人员
     */
    @Api(name = "company.deleteCompanyRecyclerById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object deleteCompanyRecyclerById(RecyclerBean recyclerBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return  companyRecyclerService.deleteCompanyRecyclerById(recyclerBean.getId(),company.getId());
    }


}
