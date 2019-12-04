package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_company_category_city_price")
@Data
public class CompanyCategoryCityPrice extends  DataEntity<Long> {

    private Long id;
    /**
     * 分类id
     */
    private  String categoryId;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 城市Id
     */
    private String cityId;

    private Long parentId;//父类id

    private String parentName;//父类id

    private String parentIds;//所有父级编号

    private String unit;//单位

    private float price; //单价
}
