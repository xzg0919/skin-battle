package com.tzj.collect.core.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayEcoActivityRecycleSendRequest;
import com.alipay.api.request.AntMerchantExpandTradeorderSyncRequest;
import com.alipay.api.response.AlipayEcoActivityRecycleSendResponse;
import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.baomidou.mybatisplus.annotations.TableName;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.constant.ApplicaInit;
import com.tzj.collect.common.mq.RocketMqConst;
import com.tzj.collect.core.param.mysl.MyslBean;
import com.tzj.collect.core.param.mysl.MyslItemBean;
import com.tzj.collect.core.service.AnsycMyslService;
import com.tzj.collect.core.service.MyslRequestLogService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.MyslRequestLog;
import com.tzj.collect.entity.Order;
import com.tzj.collect.common.notify.DingTalkNotify;
import com.tzj.module.api.annotation.AuthIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class AnsycMyslServiceImpl implements AnsycMyslService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ApplicaInit applicaInit;
    @Autowired
    MyslRequestLogService myslRequestLogService;

    @Override
    public AlipayEcoActivityRecycleSendResponse updateForest(String orderId,String myslParam, Integer times){
        times = times == null ? 0 : times;
        Order order = orderService.selectById(orderId);
        if ("true".equals(applicaInit.getIsMysl())){
            try{
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
                AlipayEcoActivityRecycleSendRequest request = new AlipayEcoActivityRecycleSendRequest();
                MyslRequestLog myslRequestLog=new MyslRequestLog();
                myslRequestLog.setOrderNo(order.getOrderNo());
                myslRequestLog.setMyslParams(myslParam);
                myslRequestLog.setOutBizNo(JSON.parseObject(myslParam).get("outBizNo").toString());
                request.setBizContent(myslParam);
                AlipayEcoActivityRecycleSendResponse response = null;
                try {
                    response = alipayClient.execute(request);
                }catch (Exception e){
                    e.printStackTrace();
                }
                myslRequestLog.setMyslResult(JSONObject.toJSON(response).toString());
                if(response.isSuccess()){
                    System.out.println("调用成功");
                    myslRequestLog.setFullEnergy(Integer.parseInt(response.getFullEnergy().toString()));
                } else {
                    if(response.getSubMsg().equals("参数有误已发放，无需再次发放！")){
                        return null;
                    }
                    ++times;
                    System.out.println("蚂蚁能量调用失败！！---递归调用次数"+times);
                    //重复调用不超过3次
                    if(times < 3) {
                        updateForest(orderId, myslParam, times);
                    }
                    DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                            ,Thread.currentThread().getStackTrace()[1].getMethodName(),"增加蚂蚁深林能量异常,订单Id是："+orderId+"次数："+times,
                            RocketMqConst.DINGDING_ERROR,response.getBody());
                }
                myslRequestLogService.insert(myslRequestLog);
                return response;
            }catch (Exception e) {
                DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                        ,Thread.currentThread().getStackTrace()[1].getMethodName(),"增加蚂蚁深林能量异常,订单Id是："+orderId,
                        RocketMqConst.DINGDING_ERROR,e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * <p>蚂蚁森林绿色能量接口</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public AlipayEcoActivityRecycleSendResponse  updateCansForest(String aliUserId,String outBizNo,long count,String type){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
        AlipayEcoActivityRecycleSendRequest request = new AlipayEcoActivityRecycleSendRequest();
        AlipayEcoActivityRecycleSendModel model = new AlipayEcoActivityRecycleSendModel();
        model.setBuyerId(aliUserId);
        model.setSellerId(AlipayConst.SellerId);
        model.setOutBizType("RECYCLING");
        model.setOutBizNo(outBizNo);
        List<EnergyGoodRequest> orderItemList = new ArrayList<EnergyGoodRequest>();
        EnergyGoodRequest itemOrder = new EnergyGoodRequest();
        itemOrder.setItemName("饮料瓶罐");
        itemOrder.setQuantity(count+"");
        List<EnergyExtRequest> extInfo = new ArrayList<>();
        EnergyExtRequest orderExtInfo = new EnergyExtRequest();
        orderExtInfo.setExtKey("ITEM_TYPE");
        orderExtInfo.setExtValue(type);
        extInfo.add(orderExtInfo);
        itemOrder.setItems(extInfo);
        orderItemList.add(itemOrder);
        model.setItemList(orderItemList);
        request.setBizModel(model);
        AlipayEcoActivityRecycleSendResponse response = null;
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



    @Override
    public   AlipayEcoActivityRecycleSendResponse updateCansForestByList(MyslBean myslBean){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
        AlipayEcoActivityRecycleSendRequest request = new AlipayEcoActivityRecycleSendRequest();
        AlipayEcoActivityRecycleSendModel model=new AlipayEcoActivityRecycleSendModel();
        model.setBuyerId(myslBean.getAliUserId());
        model.setSellerId(AlipayConst.SellerId);
        model.setOutBizType("RECYCLING");
        model.setOutBizNo(myslBean.getOutBizNo());
        List<EnergyGoodRequest> itemList = new ArrayList<>();
        List<MyslItemBean> myslItemBeans = myslBean.getMyslItemBeans();
        myslItemBeans.stream().forEach(myslItemBean -> {
            EnergyGoodRequest energyGoodRequest=new EnergyGoodRequest();
            energyGoodRequest.setItemName(myslItemBean.getItemName());
            energyGoodRequest.setQuantity(myslItemBean.getCount().toString());
            List<EnergyExtRequest> items =new ArrayList<>();
            EnergyExtRequest energyExtRequest= new EnergyExtRequest();
            energyExtRequest.setExtKey(myslItemBean.getExtKey());
            energyExtRequest.setExtValue(myslItemBean.getExtValue());
            items.add(energyExtRequest);
            energyGoodRequest.setItems(items);
            itemList.add(energyGoodRequest);
        });
        model.setItemList(itemList);
        request.setBizModel(model);
        AlipayEcoActivityRecycleSendResponse response = null;
        try {
            System.out.println("开始发放蚂蚁森林能量:"+ JSONObject.toJSON(request));
            response = alipayClient.execute(request);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("蚂蚁森林能量发放成功:"+ JSONObject.toJSON(response));

        } else {
            System.out.println("蚂蚁森林能量发放失败:"+ JSONObject.toJSON(response));
        }
        return response;
    }

    public static void main(String[] args) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
        AlipayEcoActivityRecycleSendRequest request = new AlipayEcoActivityRecycleSendRequest();
        String myslParam="{\"buyerId\":\"2088322039337350\",\"itemList\":[{\"itemName\":\"洗衣机\",\"items\":[{\"extKey\":\"ITEM_TYPE\",\"extValue\":\"appliance\"}],\"quantity\":\"1\"}],\"outBizNo\":\"20210507184917466249\",\"outBizType\":\"RECYCLING\",\"sellerId\":\"2088421446748174\"}";
        request.setBizContent(myslParam);
        AlipayEcoActivityRecycleSendResponse response = alipayClient.execute(request);
        if(response.isSuccess()){

            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}
