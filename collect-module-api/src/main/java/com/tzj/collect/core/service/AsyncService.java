package com.tzj.collect.core.service;

import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.entity.Order;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * 异步调用接口
 *
 * @Author 胡方明（12795880@qq.com）
 **/
public interface AsyncService {

    @Async
    void notifyDingDingOrderCreate(OrderBean orderBean);
    
    /**
     * 
     * <p>Created on 2017年8月14日</p>
     * <p>Description:[发送接单短信]</p>
     * @author:[王灿] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     * @param recyclerName : 回收人员名称
     * @param phone : 回收人员手机号
     * @param companyName : 企业名称
     */
    @Async
    public void sendOrder(final String freeSignName, final String moblie, final String temlateCode,
                          final String recyclerName, final String phone, final String companyName);
    /**
     *
     * <p>Created on 2017年8月14日</p>
     * <p>Description:[发送发货短信]</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     * @param goodsName : 商品名称
     * @param orderCompany : 发货快递公司C
     * @param orderNum : 快递单号
     */
    @Async
    public void sendOrderProduct(final String freeSignName, final String moblie, final String temlateCode,
                                 final String goodsName, final String orderCompany, final String orderNum);

    /**
     *
     * <p>Created on 2017年8月14日</p>
     * <p>Description:[发送接单短信]</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     * @param code : 回收人员名称
     * @param productName : 企业名称
     */
    @Async
    public void sendEnterprise(final String freeSignName, final String moblie, final String temlateCode,
                               final String code, final String productName);
    @Async
    void notifyDingDingOrderCreate(String toString, boolean b, String recruitDingDing);

    /**
     * 给的某个人发送钉钉通知
     * @param orderNo
     * @param reason
     * @param dingDingUrl
     * @param receiveTel
     */
    @Async
    void notifyDingDingPaymentError(String orderNo, String reason, String dingDingUrl,String dingDingSing ,String receiveTel);

	@Async
    void sendOpenAppMini(String aliUserId, String formId, String templateId,String page,String value1,String value2,String value3);

	@Async
    void pushOrder(Order order, MqttClient mqtt4PushOrder);
}
