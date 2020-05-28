package com.tzj.collect.core.param.ali;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 前台分类属性明细
 * @author Michael_Wang
 *
 */
@Data
public class BusinessOrderItemBean implements Serializable {
	private Long id;
    /**
     * 订单id
     */
    private  int orderId;
    /**
     * 分类id
     */
    private  int categoryId;
    /**
     * 分类名称
     */
    private  String categoryName;
    /**
     * 分类属性id
     */
    private  int categoryAttrId;
    /**
     * 分类属性名字
     */
    private String categoryAttrName;
    /**
     * 分类属性选项id
     */
    private  int categoryAttrOppId;
    /**
     * 分类属性选项名称
     */
    private  String categoryAttrOpptionName;
    
//    @TableField(exist=false) 
    private String categoryAttrOppIds;
    /**
     * 调整价
     */
    private BigDecimal adjustOrice;
    /**
     * 分类父类id
     */
    private String parentId;
    /**
     * 分类父类名称
     */
    private String parentName;
	/**
	 * 价格
	 */
	private String price;

	private String unit;//单位

    private double amount;//数量

    private double priceCount;//合价

    private Integer point;//积分单价

	/**
	 * 是否现金
	 */
	private String isCash;

	private List<BusinessOrderItemBean> businessOrderItemList;

	private String title;

	
}
