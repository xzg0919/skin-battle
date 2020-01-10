package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.ProductOrder;
import com.tzj.green.param.ProductBean;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [用户兑换订单表service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface ProductOrderService extends IService<ProductOrder>
{

    Object getProductOrderList(ProductBean productBean,Long companyId);

    Object getProductOrderDetail(String productOrderId);

}