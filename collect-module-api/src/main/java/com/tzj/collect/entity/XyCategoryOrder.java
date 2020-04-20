package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("sb_xy_category_order")
public class XyCategoryOrder extends DataEntity<Long> {

    private Long id;

    private Integer quoteId ;//咸鱼分类标记Id

    private Integer parentId;

    private String parentName;

    private Integer categoryId;

    private String categoryName;


}
