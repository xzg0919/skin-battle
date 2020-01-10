package com.tzj.green.api.ali;

import com.tzj.green.param.AreaBean;
import com.tzj.green.param.GoodsBean;
import com.tzj.green.param.ProductBean;
import com.tzj.green.service.AreaService;
import com.tzj.green.service.GoodsService;
import com.tzj.green.service.ProductService;
import com.tzj.module.api.annotation.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020-01-08 15:06
 * @Description:
 */

@ApiService
public class ActivityApi {

    @Autowired
    ProductService productService;
    @Autowired
    GoodsService goodsService;

    /**
     * 根据卡号获取用户的附近的活动
     *
     * @return
     */
    @Api(name = "activity.list", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object activityList(ProductBean productBean) {
        if (productBean.getLat() == null || productBean.getLng() == null
                || productBean.getLat() == 0 || productBean.getLng() == 0) {
            return "获取经纬度失败";
        }
        if (StringUtils.isBlank(productBean.getCompanyId())) {
            return "获取公司ID失败";
        }
        return productService.nearActivitys(productBean.getLat(), productBean.getLng(), Long.parseLong(productBean.getCompanyId()),
                productBean.getPageBean().getPageNum(), productBean.getPageBean().getPageSize());
    }


    /**
     * 获取活动详情
     *
     * @param productBean
     * @return
     */
    @Api(name = "activity.detail", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object activtyDetail(ProductBean productBean) {

        if (StringUtils.isBlank(productBean.getProductNo())) {
            return "获取活动编号失败";
        }
        return productService.activtyDetail(productBean.getProductNo());
    }


    /**
     * 礼品明细
     *
     * @param goodsBean
     * @return
     */
    @Api(name = "activity.goodsDetail", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object goodsDetail(GoodsBean goodsBean) {
        if (StringUtils.isBlank(goodsBean.getGoodsNo())) {
            return "获取礼品编号失败";
        }
        return goodsService.getGoodsDetail(goodsBean.getGoodsNo());
    }


}
