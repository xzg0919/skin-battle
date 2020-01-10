package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.Product;
import com.tzj.green.param.ProductBean;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [活动表service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface ProductService extends IService<Product>
{

    Object saveOrUpdateProduct(ProductBean productBean, Long companyId);

    Object getProductList(ProductBean productBean,Long companyId);

    Object getProductDetailById(ProductBean productBean);

    Object updateProductIsLowerById(String productId,String isLower);

    Object nearActivitys(Double lat,Double lng,Long companyId);

    Object getGoodsListByCompanyId(Long companyId,ProductBean productBean);

}