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
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Area;
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

    @Override
    public AlipayEcoActivityRecycleSendResponse updateForest(String orderId,String myslParam, Integer times){
        times = times == null ? 0 : times;
        Order order = orderService.selectById(orderId);
        if ("true".equals(applicaInit.getIsMysl())){
            try{
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
                AlipayEcoActivityRecycleSendRequest request = new AlipayEcoActivityRecycleSendRequest();
                AlipayEcoActivityRecycleSendModel model = new AlipayEcoActivityRecycleSendModel();
                request.setBizContent(myslParam);
                AlipayEcoActivityRecycleSendResponse response = null;
                try {
                    response = alipayClient.execute(request);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(response.isSuccess()){
                    System.out.println("调用成功");
                    String fullEnergy = response.getFullEnergy().toString();
                    order.setMyslOrderId(fullEnergy);
                    order.setMyslParam(JSON.toJSONString(response.getParams()));
                    orderService.updateById(order);
                } else {
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
        String private_key ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCSgg/+FkPQyzteFgSyrDCtI3h56DbMT2I6F5OcRay9lWX+qHzRSDQ0w6YLUk1QnwuHchJynke5Iuo1mkLTD5YE4esjc8qZy1ebBnaklTufGsD31TzXOgNXNGT+RUjvEV/jCtsotAz+lGFdA3dAR4VEoIceLLF5Onrx2Cj60NG227YuJLWaSt7cOa1RCOA+eWnmewXtyHAp7QEjPeQsqO8hDI1pWlvq7GqZwfpAOT6GdFvyOR7GTqCf7OywQWJNpZvHeJBmXcQYU/7FunNGi3NXhJZ+owuGJhE3wmt09CXHhapZF7i5Q1PjKbsXRl0n4CG1M2ZKfgK8AJq6RtwaJWvzAgMBAAECggEAHHSomIhIibA/iqmqJe3t0mMWTk88/XfZs0DLCco6n+P5mHmvLJLimPD7Oi21GJibi6YrURnPAnV2mgypOAdJ8w7SNeOSkUZ+cHzT5Gmb1+5BVqGTHJaG+ZQSnZRlcxGj1xxPrtsxvuyg8Ijwy4pYpxiqxcCWg95I7No4M2cFpIdChN15Xzv1IT+2crWtibm9+w+RlDInkF53SGFYcnp7Kygi1N5MQp/TXtxjLkDapN0gN4Se3afPmRyAiUa75tfcrkWqP4b0XJVRVMcuLheWr1e7F5zNhlXDgSF+4CDBjNDOkvyishlfI4iWq5XcegwR7IE4Bnsu+24K5gyZFX5DwQKBgQDVzTP8LqGmdvRvESxcBP9PsDjW65FsxuhrF7usVJEIBQqGsxv8IrmjbrRh0S8klg0oCR+CTmyUVgG7K6DNE5g2xH4hIHLpsfusGMZ5ZYpvkZPkdVCdAtzgfVPrCDlpmxhmIdX1/O29th+DQWCOnQyQMbzzXnyi06MT+rc0ZU314QKBgQCvbLSqmqzaaP6JenGeXIaY+qMHLCH7Oyf9TP/WoN5Ump/IEHYkEodoVBDAix1PV0dDlakUTric8+ZLFZYAKx07YMWH4sNqpi9/iuTVOu1pE5Ujjyqgzx7rSQCh0UZGh5Cx+AB3ADrF7DSrZJ5jsQHdpPIQYZ0RqVCrINSs3400UwKBgQCQrg1YwUGYwXTF2Ew5dRREm87K+ZxujOfo2KWih5OhZq/p2Ti91c3j391nOER7sKL0jb/p0SI++ziurWZigLkHjs5/olLA/DWADz/4FJOqzkvTVOQZbD+GFql8KhOX+GF5c7ozfVZwg9ctXkd2GqbOySnBUxZqpWb89TOW3Fr2YQKBgQClrmzACNAihDGLFKsEn2y8RVP+bS83AbsWIEgtpMf3bgZkxS9fiGR9I5Ci2YD4M31qFoqXVHZGPpEN7zg5tn6oWxCU85YPPx/zPPI9dadOq6Ea8ZeYQ6Z3H/7J1kaiTF1byNhfjEb2Mc3Y5nI1w7L4bFX3JMpdKOW4ioxzLbxGYwKBgDBjUEMLytcZILhuSK5T7RRVabQEt1EdgLbdNpbB8aBuIfPuEHtneEjYkldomPwwRFsj3fdhFX/dTVA6W/Os7ObOMrl4VW1gR8NFaxliBhn98bQAUhIufE7y6xACJdaTEi4b8aKhEYMG2GRduc2E/ojNlnSvYUwqMwopjM13bG+f";
        String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvrrfh6MpywjtSAF9+w2IOUizb+Y0qojIucmADlB6SCQpGYS6j0vSGBsqvk55RQZu3BVgCpwKu3Fe/HH5IGCXb8MYn8rvAhx97otBssVqqS3ruc1RMNmP6Mu8lHnV0pzYESHIHe20ATBdID7+NToV6h7vAKEf3wdyNJBB/nJ7VTvF0HywQ6djsnjySppwg5XTrA2IvWhxHDbRxwSzrvuBw4JDg6U6A4VdrIn54b32FPcCnRZSus+EIXDB2i5+eq6gODPd2gNRHet4mLdOLva0gpmLh7JFX4GYGvwqpgX+x1SFfoBGIsNghG/tU0eTD3p0fA4fA8uQgdNtT35bGn2ogQIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient("http://openapi.stable.dl.alipaydev.com/gateway.do",
                "2018062260108473",private_key,"json","GBK",publicKey,"RSA2");
        AlipayEcoActivityRecycleSendRequest request = new AlipayEcoActivityRecycleSendRequest();
        AlipayEcoActivityRecycleSendModel model=new AlipayEcoActivityRecycleSendModel();
        model.setBuyerId("2088302944637651");
        model.setSellerId("2088302088119497");
        model.setOutBizType("RECYCLING");
        model.setOutBizNo("202104067898637836378");
        List<EnergyGoodRequest> itemList = new ArrayList<>();
        List<EnergyExtRequest> items =new ArrayList<>();
        EnergyGoodRequest energyGoodRequest=new EnergyGoodRequest();
        energyGoodRequest.setItemName("书本");
        energyGoodRequest.setQuantity("3.0");
        EnergyExtRequest energyExtRequest= new EnergyExtRequest();
        energyExtRequest.setExtKey("ITEM_TYPE");
        energyExtRequest.setExtValue("paper");
        items.add(energyExtRequest);
        energyGoodRequest.setItems(items);
        itemList.add(energyGoodRequest);
        model.setItemList(itemList);
        request.setBizModel(model);
        AlipayEcoActivityRecycleSendResponse response = alipayClient.execute(request);
        if(response.isSuccess()){

            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}
