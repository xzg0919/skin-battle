package com.tzj.green.api.company;

import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Company;
import com.tzj.green.entity.Product;
import com.tzj.green.param.ProductBean;
import com.tzj.green.service.ProductService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class ProduceApi {

    @Autowired
    private ProductService productService;

    /**
     */
    @Api(name = "company.saveOrUpdateProduct", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object saveOrUpdateProduct(ProductBean productBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return  productService.saveOrUpdateProduct(productBean,company.getId());
    }

    /**
     *获取活动列表
     */
    @Api(name = "company.getProductList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getProductList(ProductBean productBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return  productService.getProductList(productBean,company.getId());
    }
    /**
     *根据活动Id查询活动详情
     */
    @Api(name = "company.getProductDetailById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getProductDetailById(ProductBean productBean) {
        return  productService.getProductDetailById(productBean);
    }

    /**
     *根据活动Id上下架活动
     */
    @Api(name = "company.updateProductIsLowerById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object updateProductIsLowerById(ProductBean productBean) {
        return  productService.updateProductIsLowerById(productBean.getId(),productBean.getIsLower());
    }
    /**
     *获取该公司的商品
     */
    @Api(name = "company.getGoodsListByCompanyId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getGoodsListByCompanyId(ProductBean productBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return  productService.getGoodsListByCompanyId(company.getId(),productBean);
    }




}
