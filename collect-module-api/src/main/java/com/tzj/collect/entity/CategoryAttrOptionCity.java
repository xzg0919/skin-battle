package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

@TableName("sb_category_attr_option_city")
@Data
public class CategoryAttrOptionCity extends  DataEntity<Long> {

    private Long id;

    /**
     * 城市Id
     */
    private String cityId;
    /**
     * 分类属性选项id
     */
    private Integer categoryAttrOptionId;
    /**
     * 属性增减价格
     */
    private BigDecimal attrOptionPrice;
    /**
     * 是否回收 0回收  1不回收
     */
    private String isRecovery = "0";
    /**
     * 是否是特殊标签 0不是  1是
     */
    private String isSpecial = "0";
    /**
     * 特殊价格
     */
    private BigDecimal specialPrice;



}
