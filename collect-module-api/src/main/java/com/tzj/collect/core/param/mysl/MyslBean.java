package com.tzj.collect.core.param.mysl;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MyslBean implements Serializable {

    //用户的aliuserId
    private String aliUserId;
    //外部订单号
    private String outBizNo;

    private List<MyslItemBean> myslItemBeans;


}
