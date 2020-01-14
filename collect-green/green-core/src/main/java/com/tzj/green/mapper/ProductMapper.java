package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [活动表映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface ProductMapper extends BaseMapper<Product>
{
    Map<String,Object> getProductDetailById(@Param("productId") Long productId);

    List<Map<String,Object>> getProductGoodsList(@Param("productId") Long productId,@Param("pageStart") Integer pageStart,@Param("pageSize") Integer pageSize);

    Integer getProductGoodsCount(@Param("productId") Long productId);

    List<Map<String,Object>> getRecyclerList(@Param("productId") Long productId);

    List<Map<String,Object>> nearActivitys(@Param("companyId") Long companyId,@Param("lat") double lat,@Param("lng") double lng,@Param("pageStartNum") Integer pageStartNum, @Param("pageSize") Integer pageSize);

    List<Map<String,Object>> getGoodsListByCompanyId(@Param("companyId")Long companyId,@Param("name")String name,@Param("pageStart") Integer pageStart,@Param("pageSize")Integer pageSize);

   Integer getGoodsCount(@Param("companyId")Long companyId,@Param("name")String name);


    Map<String,Object> activityDetail(@Param("productNo") String productNo);

}