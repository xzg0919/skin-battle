package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

@TableName("sb_company_category_attr_option_city_price")
@Data
public class CompanyCategoryAttrOptionCityPrice extends  DataEntity<Long> {

    private Long id;

    /**
     * 公司ID
     */
    private String companyId;
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
    private String isRecovery;
    /**
     * 是否是特殊标签 0不是  1是
     */
    private String isSpecial;
    /**
     * 特殊价格
     */
    private BigDecimal specialPrice;

}
