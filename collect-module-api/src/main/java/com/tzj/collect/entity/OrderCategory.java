package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("sb_order_category")
public class OrderCategory extends  DataEntity<Long>{

    private Long id;

    private Integer orderId;

    private Integer categoryId;



}
