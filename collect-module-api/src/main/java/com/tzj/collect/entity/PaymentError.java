package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_payment_error")
@Data
public class PaymentError extends DataEntity<Long>{

    public final static  String DING_DING_URL = "https://oapi.dingtalk.com/robot/send?access_token=caa7be924d7c548737b73d72f83c6399af686a727d36d23d0fd4d52f1136750e";    //未支付
    public final static  String DING_DING_SING = "SECed99b4d9d7e0516b89a1de9e2d496a8eda697ebc00195204e826449ca20a0fae";
    public final static  String DING_DING_TEL = "17621084242";

    private Long id;

    private String orderSn;//'订单编号',

    private String title;//'标题',

    private String reason;//'内容',

    private String notifyType;//'通知类型',

    private String receiveUser;//'接收人员',

    private String sendUser;//'发送人员'




}
