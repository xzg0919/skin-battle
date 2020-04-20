package com.tzj.collect.core.param.xianyu;

import lombok.Data;

@Data
public class QiMemOrder {

    public static final String QI_MEM_ORDER_SUCCESS = "5";

    public static final String QI_MEM_ORDER_CANCEL = "103";

    //取消原因
    private String reason;
    //代表最终成交时的价格单位分 status=103必传
    private String confirmFee;
    //数量    status=103必传
    private String quantity;
    //外部订单号
    private String bizOrderId;
    //5完成订单  103服务商取消订单
    private String orderStatus;


}
