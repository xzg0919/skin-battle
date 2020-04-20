package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.Goods;
import com.tzj.green.param.GoodsBean;

import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [商品表service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface GoodsService extends IService<Goods>
{

    Object saveOrUpdateGoods(GoodsBean goodsBean,Long companyId);

    Object getGoodsList(GoodsBean goodsBean,Long companyId);

    Object getGoodsListByActivityId(String activityCode);

    Object getGoodsDetail(String goodsNo);

    Map<String, Object> selectAddressByProId(Long proId);
}