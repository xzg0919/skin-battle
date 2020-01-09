package com.tzj.green.api.company;


import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Company;
import com.tzj.green.param.GoodsBean;
import com.tzj.green.param.PointsListBean;
import com.tzj.green.service.PointsListService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class PointsListApi {

    @Resource
    private PointsListService pointsListService;

    /**
     * 新商品信息
     */
    @Api(name = "company.getPointsListByCompanyId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getPointsListByCompanyId(PointsListBean pointsListBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return pointsListService.getPointsListByCompanyId(company.getId(),pointsListBean);
    }



}
