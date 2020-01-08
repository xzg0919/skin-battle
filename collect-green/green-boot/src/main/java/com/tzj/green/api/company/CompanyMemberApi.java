package com.tzj.green.api.company;


import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Company;
import com.tzj.green.param.CompanyCommunityBean;
import com.tzj.green.param.MemberBean;
import com.tzj.green.service.MemberService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class CompanyMemberApi {

    @Resource
    private MemberService memberService;


    /**
     * 获取用户列表
     */
    @Api(name = "company.getMemberList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getMemberList(MemberBean memberBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return memberService.getMemberList(memberBean,company.getId());
    }

    /**
     * 注销用户
     */
    @Api(name = "company.updateIsCancelByRealNo", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object updateIsCancelByRealNo(MemberBean memberBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return memberService.updateIsCancelByRealNo(memberBean.getRealNo());
    }



}
