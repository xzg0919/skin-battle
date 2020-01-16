package com.tzj.green.api.company;


import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Company;
import com.tzj.green.param.CompanyCommunityBean;
import com.tzj.green.service.CommunityHouseNameService;
import com.tzj.green.service.CompanyCommunityService;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class CompanyCommunityApi {

    @Autowired
    private CompanyCommunityService companyCommunityService;
    @Autowired
    private CommunityHouseNameService communityHouseNameService;

    /**
     * 保存或更新社区信息
     */
    @Api(name = "company.saveOrUpdateCompanyCommunity", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object saveCompanyCommunity(CompanyCommunityBean companyCommunityBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return companyCommunityService.saveCompanyCommunity(companyCommunityBean,company.getId());
    }
    /**
     * 根据社区Id查询社区相关信息
     */
    @Api(name = "company.getCompanyCommunityById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getCompanyCommunityById(CompanyCommunityBean companyCommunityBean) {
        return companyCommunityService.getCompanyCommunityById(companyCommunityBean.getId());
    }
    /**
     * 根据小区名称Id删除小区名称信息
     */
    @Api(name = "company.deleteCommunityHouseNameById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object deleteCommunityHouseNameById(CompanyCommunityBean companyCommunityBean) {
        return communityHouseNameService.deleteById(Long.parseLong(companyCommunityBean.getId()));
    }

    /**
     * 根据各种条件获取相关居委的信息
     */
    @Api(name = "company.getCompanyCommunityList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getCompanyCommunityList(CompanyCommunityBean companyCommunityBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return companyCommunityService.getCompanyCommunityList(companyCommunityBean,company.getId());
    }

    /**
     * 根据街道id 获取居委信息
     */
    @Api(name = "company.getCompanyCommunityListByStreetId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getCompanyCommunityListByStreetId(CompanyCommunityBean companyCommunityBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return companyCommunityService.getCompanyCommunityListByStreetId(companyCommunityBean,company.getId());
    }
    /**
     * 根据居委Id获取小区信息
     */
    @Api(name = "company.getCompanyHouseListByCommunityId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getCompanyHouseListByCommunityId(CompanyCommunityBean companyCommunityBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return companyCommunityService.getCompanyHouseListByCommunityId(companyCommunityBean,company.getId());
    }

    /**
     * 根据社区Id 相关回收人员信息
     */
    @Api(name = "company.getRecyclerListByHouseId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getRecyclerListByHouseId(CompanyCommunityBean companyCommunityBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return companyCommunityService.getRecyclerListByHouseId(companyCommunityBean,company.getId());
    }



}
