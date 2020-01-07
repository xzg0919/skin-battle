package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [商品活动关联表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_product_goods")
public class ProductGoods extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 商品Id
     */
    private Long goodsId;
    /**
     * 活动Id
     */
    private Long productId;
    /**
     * 商品分配总数量
     */
    private Long totalNum;
    /**
     * 商品兑换数量
     */
    private Long exchangeNum = (long)0;


}
