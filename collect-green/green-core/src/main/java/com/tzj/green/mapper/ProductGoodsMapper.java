package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.ProductGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [商品活动关联表映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface ProductGoodsMapper extends BaseMapper<ProductGoods>
{

    List<Map<String, Object>> appProductList(@Param("recId") Long recId);

    List<Map<String, Object>> appGoodsList(@Param("proId")Long proId);

    Map<String, Object> goodsAmount(@Param("proId")Long productId, @Param("godId")Long goodsId);
}