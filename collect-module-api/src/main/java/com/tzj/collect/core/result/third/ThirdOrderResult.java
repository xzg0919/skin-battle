package com.tzj.collect.core.result.third;

import lombok.Data;

/**
 * Created on 2019/8/13
 * Title: TODO
 * Description: TODO
 * Copyright: Copyright (c) 2019
 * Company: 上海铸乾信息科技有限公司
 * Department:研发部
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@Data
public class ThirdOrderResult {
    //订单号
    private String orderNo;
    //三级
    private String level3;
    //二级
    private String level2;
    //一级
    private String level1;
    //绿色能量
    private String greenCount;
    //数量
    private String amount;
}
