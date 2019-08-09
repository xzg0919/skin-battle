package com.tzj.collect.core.service.impl;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AntMerchantExpandTradeorderSyncModel;
import com.alipay.api.domain.ItemOrder;
import com.alipay.api.domain.OrderExtInfo;
import com.alipay.api.request.AntMerchantExpandTradeorderSyncRequest;
import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.constant.RocketMqConst;
import com.tzj.collect.core.service.AnsycMyslService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Order;
import com.tzj.module.common.notify.dingtalk.DingTalkNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnsycMyslServiceImpl implements AnsycMyslService {

    @Autowired
    private OrderService orderService;


    @Override
    public AntMerchantExpandTradeorderSyncResponse updateForest(String orderId,String myslParam){
        Order order = orderService.selectById(orderId);
        try{
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
            AntMerchantExpandTradeorderSyncRequest request = new AntMerchantExpandTradeorderSyncRequest();
            AntMerchantExpandTradeorderSyncModel model = new AntMerchantExpandTradeorderSyncModel();
            request.setBizContent(myslParam);
            AntMerchantExpandTradeorderSyncResponse response = null;
            try {
                response = alipayClient.execute(request);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(response.isSuccess()){
                System.out.println("调⽤成功");
                String MorderId = response.getOrderId();
                order.setMyslOrderId(MorderId);
                order.setMyslParam(JSON.toJSONString(response.getParams()));
                orderService.updateById(order);
            } else {
                System.out.println("调⽤失败");
                DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                        ,Thread.currentThread().getStackTrace()[1].getMethodName(),"增加蚂蚁深林能量异常,订单Id是："+orderId,
                        RocketMqConst.DINGDING_ERROR,response.getBody());
            }
            return response;
        }catch (Exception e) {
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"增加蚂蚁深林能量异常,订单Id是："+orderId,
                    RocketMqConst.DINGDING_ERROR,e.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>蚂蚁森林绿色能量接口</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public AntMerchantExpandTradeorderSyncResponse  updateCansForest(String aliUserId,String outBizNo,long count,String type){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
        AntMerchantExpandTradeorderSyncRequest request = new AntMerchantExpandTradeorderSyncRequest();
        AntMerchantExpandTradeorderSyncModel model = new AntMerchantExpandTradeorderSyncModel();
        model.setBuyerId(aliUserId);
        model.setSellerId(AlipayConst.SellerId);
        model.setOutBizType("RECYCLING");
        model.setOutBizNo(outBizNo);
        List<ItemOrder> orderItemList = new ArrayList<ItemOrder>();
        ItemOrder itemOrder = new ItemOrder();
        itemOrder.setItemName("饮料瓶罐");
        itemOrder.setQuantity(count);
        List<OrderExtInfo> extInfo = new ArrayList<>();
        OrderExtInfo orderExtInfo = new OrderExtInfo();
        orderExtInfo.setExtKey("ITEM_TYPE");
        orderExtInfo.setExtValue(type);
        extInfo.add(orderExtInfo);
        itemOrder.setExtInfo(extInfo);
        orderItemList.add(itemOrder);
        model.setItemOrderList(orderItemList);
        request.setBizModel(model);
        AntMerchantExpandTradeorderSyncResponse response = null;
        try {
            response = alipayClient.execute(request);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调⽤成功");

        } else {
            System.out.println("调⽤失败");
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"塑料瓶罐---增加蚂蚁深林能量异常",
                    RocketMqConst.DINGDING_ERROR,response.getBody());
        }
        return response;
    }
}
