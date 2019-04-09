package com.tzj.collect.controller.admin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.api.iot.param.IotParamBean;
import com.tzj.collect.common.constant.RocketMqConst;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.RocketmqMessage;
import com.tzj.collect.service.OrderService;
import com.tzj.collect.service.RocketmqMessageService;
import com.tzj.module.common.aliyun.mns.Notification;
import com.tzj.module.common.aliyun.mns.XMLUtils;
import com.tzj.module.common.notify.dingtalk.DingTalkNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("iot")
public class IotController {

    @Autowired
    private RocketmqMessageService rocketmqMessageService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/order",method = RequestMethod.POST)
    public String orderUpdate(@RequestBody String body, HttpServletResponse response){
        System.out.println("收到的body-----"+body);
        Notification notification=null;
        IotParamBean iotParamBean = null;
        try {
            notification = RocketMqConst.processMNSMessage(body, RocketMqConst.TOPIC_NAME_IOT_ORDER);
            JSONObject object = JSON.parseObject(notification.getMessage());

            iotParamBean = new IotParamBean();
            iotParamBean.setSumPrice(new BigDecimal(object.get("sumPrice").toString()));
            iotParamBean.setEquipmentCode(object.get("equipmentCode").toString());
            iotParamBean.setMemberId(object.get("memberId").toString());
            iotParamBean.setParentLists(object.getJSONArray("parentLists").toJavaList(IotParamBean.ParentList.class));
        } catch (Exception e) {
            //说明接收到的消息有问题，直接丢弃掉
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"message有问题:",
                    RocketMqConst.DINGDING_ERROR,e.toString()+body);
            return null;
        }
        try{
            RocketmqMessage rocketmqMessage = new RocketmqMessage();
            rocketmqMessage.setMessageId(notification.getMessageId());
            rocketmqMessage.setMessage(notification.getMessage());
            rocketmqMessageService.insert(rocketmqMessage);
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"保存notification异常",
                    RocketMqConst.DINGDING_ERROR,e.toString());
            return null;
        }
        try {
            //消息保存成功之后再处理
            orderService.iotCreatOrder(iotParamBean);
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"保存数据异常",
                    RocketMqConst.DINGDING_ERROR,e.toString());
            return  null;
        }
        //消息消费成功
        response.setStatus(HttpServletResponse.SC_OK);

        System.out.println("消息通知结束---------------------------");
        return null;
    }


    public static void main(String[] args) {
        HashMap<String,Object> param=new HashMap<>();
        IotParamBean.ItemList itemList1 = new IotParamBean.ItemList();
        itemList1.setName(Category.SecondType.BOOK_MAGAZINE);
        itemList1.setPrice(3.40f);
        itemList1.setQuantity(2);
        itemList1.setUnit("kg");
        IotParamBean.ItemList itemList2 = new IotParamBean.ItemList();
        itemList2.setName(Category.SecondType.CARD_BOARD_BOXES);
        itemList2.setPrice(5.20f);
        itemList2.setQuantity(2);
        itemList2.setUnit("kg");
        List<IotParamBean.ItemList> itemLists = new ArrayList<>();
        itemLists.add(itemList1);
        itemLists.add(itemList2);

        IotParamBean.ParentList parentList = new IotParamBean.ParentList();
        parentList.setParentName(Category.ParentType.PAPER);
        parentList.setItemList(itemLists);
        List<IotParamBean.ParentList> parentLists = new ArrayList<>();
        parentLists.add(parentList);

        param.put("memberId", 6786);
        param.put("equipmentCode", "ceshi_code");
        param.put("sumPrice",17.20);
        param.put("parentLists", parentLists);
        RocketMqConst.sendDeliveryOrder("{\\\"memberId\\\": \\\"6786\\\",\\\"equipmentCode\\\": \\\"kw_1232\\\",\\\"sumPrice\\\": 17.00,\\\"parentLists\\\": [{\\\"parentName\\\": \\\"PAPER\\\",\\\"itemList\\\": [{\\\"name\\\": \\\"BOOK_MAGAZINE\\\",\\\"quantity\\\": \\\"2\\\",\\\"unit\\\": \\\"kg\\\",\\\"price\\\": 3.40},{\\\"name\\\": \\\"CARD_BOARD_BOXES\\\",\\\"quantity\\\": \\\"2\\\",\\\"unit\\\": \\\"kg\\\",\\\"price\\\": 5.20}]}]}",RocketMqConst.TOPIC_NAME_IOT_ORDER);
        //JSONObject object = JSON.parseObject(JSON.toJSONString(param));
//        Notification notification = xmlTest();
//        JSONObject object = JSON.parseObject(notification.getMessage());
//        IotParamBean iotParamBean = null;
//        iotParamBean = new IotParamBean();
//        iotParamBean.setSumPrice(new BigDecimal(object.get("sumPrice").toString()));
//        iotParamBean.setEquipmentCode(object.get("equipmentCode").toString());
//        iotParamBean.setMemberId(object.get("memberId").toString());
//        iotParamBean.setParentLists(object.getJSONArray("parentLists").toJavaList(IotParamBean.ParentList.class));
//        System.out.println(iotParamBean.getParentLists());
    }
    private static Notification xmlTest() {
        String xml="<Notification xmlns=\"http://mns.aliyuncs.com/doc/v1/\">\n" +
                "  <TopicOwner>1804870195031869</TopicOwner>\n" +
                "  <TopicName>JQCRM</TopicName>\n" +
                "  <Subscriber>1804870195031869</Subscriber>\n" +
                "  <SubscriptionName>sub-http-jqcrm</SubscriptionName>\n" +
                "  <MessageId>FBA0438BE5D9BBD8-2-15FDC6B733F-2000001C7</MessageId>\n" +
                "  <MessageMD5>CC3DC57843466ED85CEECA88D7A326C0</MessageMD5>\n" +
                "  <Message>{\"sumPrice\":17.2,\"equipmentCode\":\"kw_1232\",\"parentLists\":[{\"itemList\":[{\"name\":\"BOOK_MAGAZINE\",\"price\":3.4,\"quantity\":2.0,\"unit\":\"kg\"},{\"name\":\"CARD_BOARD_BOXES\",\"price\":5.2,\"quantity\":2.0,\"unit\":\"kg\"}],\"parentName\":\"PAPER\"}],\"memberId\":6786}</Message>\n" +
                "  <PublishTime>1511231550271</PublishTime>\n" +
                "  <SigningCertURL>https://mnstest.oss-cn-hangzhou.aliyuncs.com/x509_public_certificate.pem</SigningCertURL>\n" +
                "</Notification>";

        Notification notification = XMLUtils.parse(xml);
        System.out.println(notification.getMessageId());
        System.out.println(notification.getMessage());
        return notification;
    }


}
