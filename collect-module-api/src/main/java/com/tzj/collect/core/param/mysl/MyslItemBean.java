package com.tzj.collect.core.param.mysl;

import lombok.Data;

import java.io.Serializable;

@Data
public class MyslItemBean implements Serializable {

    //取分类表里面的类型
    private String extValue;

    private String extKey = "ITEM_TYPE";
    //具体的物品描述
    private String itemName;
    //数量
    private Long count;


}
