package com.tzj.green.api.company;

import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Company;
import com.tzj.green.param.ProductBean;
import com.tzj.green.service.ProductOrderService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class ProductOrderApi {

    @Resource
    private ProductOrderService productOrderService;


    /**
     *获取活动列表
     */
    @Api(name = "company.getProductOrderList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getProductOrderList(ProductBean productBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return  productOrderService.getProductOrderList(productBean,company.getId());
    }
    /**
     *根据活动Id获取活动详情
     */
    @Api(name = "company.getProductOrderDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getProductOrderDetail(ProductBean productBean) {
        return  productOrderService.getProductOrderDetail(productBean.getId());
    }


}
