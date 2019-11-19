package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("sb_company_category_city_locale")
public class CompanyCategoryCityLocale extends  DataEntity<Long> {
    private Long id;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 城市Id
     */
    private String cityId;
    /**
     * 分类Id
     */
    private Integer categoryId;
    /**
     * 父级编号
     */
    private Integer parentId;
    /**
     * 父级名称
     */
    private String parentName;
    /**
     * 所有父级编号
     */
    private String parentIds;
    /**
     * 回收单价
     */
    private BigDecimal price;
    /**
     * 计量单位
     */
    private String unit;
}
