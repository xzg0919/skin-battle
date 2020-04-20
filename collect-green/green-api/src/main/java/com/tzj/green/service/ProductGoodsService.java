package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.ProductGoods;
import com.tzj.green.param.MemberGoodsBean;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [商品活动关联表service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface ProductGoodsService extends IService<ProductGoods>
{

    Set<Map<String, Object>> appGoodsList(Long id);

    List<Map<String, Object>> appGoodsListByProId(String proId);

    List<Map<String, Object>> appProductList(Long recId);

    Map<String, Object> appGoodsChange(MemberGoodsBean memberGoodsBean);
}