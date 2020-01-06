package com.tzj.green.api.company;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Company;
import com.tzj.green.entity.Goods;
import com.tzj.green.param.CompanyCommunityBean;
import com.tzj.green.param.GoodsBean;
import com.tzj.green.param.PageBean;
import com.tzj.green.service.GoodsService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class GoodsApi {

    @Autowired
    private GoodsService goodsService;

    /**
     * 保存或更新商品信息
     */
    @Api(name = "company.saveOrUpdateGoods", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object saveCompanyCommunity(GoodsBean goodsBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return goodsService.saveOrUpdateGoods(goodsBean,company.getId());
    }

    /**
     * 新商品信息
     */
    @Api(name = "company.getGoodsDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getGoodsDetail(GoodsBean goodsBean) {
        return goodsService.selectById(goodsBean.getId());
    }

    /**
     * 新商品列表
     */
    @Api(name = "company.getGoodsList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getGoodsList(GoodsBean goodsBean) {
        Company company = CompanyUtils.getCompanyAccount();
        return goodsService.getGoodsList(goodsBean,company.getId());
    }

    /**
     * 商品删除
     */
    @Api(name = "company.deleteGoodsById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object deleteGoodsById(GoodsBean goodsBean) {
        return goodsService.deleteById(Long.parseLong(goodsBean.getId()));
    }

}
