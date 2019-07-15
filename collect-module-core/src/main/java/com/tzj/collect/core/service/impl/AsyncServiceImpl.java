package com.tzj.collect.core.service.impl;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.AsyncService;
import com.tzj.module.common.notify.dingtalk.DingTalkNotify;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @Author 胡方明（12795880@qq.com）
 *
 * 异步通知
 *
 **/
@Service
public class AsyncServiceImpl implements AsyncService {
    @Override
    @Async
    public void notifyDingDingOrderCreate(OrderBean orderBean) {
        StringBuffer message=new StringBuffer();
        message.append("收呗有新订单啦！\r\n");
        message.append("订单号：").append(orderBean.getOrderNo()).append("\r\n");
        message.append("企业：").append(orderBean.getCompanyName());
        ArrayList<String> atMobiles=new ArrayList<>();
        atMobiles.add("15996602085");
        atMobiles.add("13501675585");
        atMobiles.add("15901929893");
        atMobiles.add("17702196566");
        atMobiles.add("17717939653");
        atMobiles.add("13301857305");
        atMobiles.add("13855829796");
        atMobiles.add("15121063188");
        atMobiles.add("13262790702");
        DingTalkNotify.sendTextMessageWithAt(message.toString(),atMobiles, orderBean.getDingDingUrl());
    }
    @Async
    public void notifyDingDingOrderCreate(String message, boolean atAll,  String dingDingUrl) {
        DingTalkNotify.sendTextMessageWithAtAndAtAll(message, null, atAll, dingDingUrl);
    }
    
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
    @Override
    @Async
    public void sendOrder(final String freeSignName, final String moblie, final String temlateCode, 
            final String recyclerName,final String phone,final String companyName)
    {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"name\":\"" + recyclerName + "\",\"phone\":\""+phone+"\",\"company\":\""+companyName+"\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try
        {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * 
     * <p>Created on 2017年8月14日</p>
     * <p>Description:[发送发货短信]</p>
     * @author:[王灿] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     */
    @Override
    @Async
    public void sendOrderProduct(final String freeSignName, final String moblie, final String temlateCode, 
            final String goodsName,final String orderCompany,final String orderNum)
    {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"goods\":\"" + goodsName + "\",\"order_company\":\""+orderCompany+"\",\"order_num\":\""+orderNum+"\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try
        {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }
    }
    /**
     *
     * <p>Created on 2017年8月14日</p>
     * <p>Description:[发送发货短信]</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     */
    @Override
    @Async
    public void sendEnterprise(final String freeSignName, final String moblie, final String temlateCode,
                               final String code,final String productName)
    {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"code\":\"" + code + "\",\"productName\":\""+productName+"\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try
        {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }
    }


}
