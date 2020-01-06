package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [用户兑换订单表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_product_order")
public class ProductOrder extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 所属的企业Id
     */
    private Long companyId;
    /**
     * 所属回收人员Id
     */
    private Long recyclerId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户卡号
     */
    private String userNo;
    /**
     * userid
     */
    private String aliUserId;
    /**
     * 手机号
     */
    private String tel;
    /**
     * 消耗积分数量
     */
    private Long points;
    /**
     * 所属活动id
     */
    private Long productId;
    /**
     * 所属活动名称
     */
    private Long productName;
    /**
     * 兑换的商品id
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 兑换商品数量
     */
    private Long goodsNum;
    /**
     * 用户地址
     */
    private String address;


}
