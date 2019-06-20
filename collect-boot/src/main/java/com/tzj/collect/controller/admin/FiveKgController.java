package com.tzj.collect.controller.admin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.common.constant.RocketMqConst;
import com.tzj.collect.entity.*;
import com.tzj.collect.service.*;
import com.tzj.module.common.aliyun.mns.Notification;
import com.tzj.module.common.aliyun.mns.XMLUtils;
import com.tzj.module.common.exception.BusiException;
import com.tzj.module.common.notify.dingtalk.DingTalkNotify;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("fivekg")
public class FiveKgController {

    @Autowired
    private RocketmqMessageService rocketmqMessageService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemAchService orderItemAchService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderPicService orderPicService;
    @Autowired
    private OrderPicAchService orderPicAchService;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private AnsycMyslService ansycMyslService;


    @RequestMapping(value = "/order/update",method = RequestMethod.POST)
    public String orderUpdate(@RequestBody String body, HttpServletResponse response){
        System.out.println("收到的body-----"+body);
        Notification notification=null;
        try {
            notification=processMNSMessage(body, RocketMqConst.TOPIC_NAME_RETURN_ORDER);
        } catch (Exception e) {
            //说明接收到的消息有问题，直接丢弃掉
            System.out.println("message有问题:"+e.getMessage());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"message有问题:",
                    RocketMqConst.DINGDING_ERROR,e.toString()+body);
            return null;
        }
        String message = notification.getMessage();
        JSONObject object = JSON.parseObject(message);
        String orderStatus = object.getString("orderStatus");
        String orderNo = object.getString("orderNo");
        if (StringUtils.isBlank(orderStatus)||StringUtils.isBlank(orderNo)){
            return null;
        }
        Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", orderNo).eq("del_flag", 0));
        if("2".equals(orderStatus)){
            if((Order.OrderType.COMPLETE+"").equals(order.getStatus()+"")||(Order.OrderType.CANCEL+"").equals(order.getStatus()+"")||(Order.OrderType.REJECTED+"").equals(order.getStatus()+"")){
                return null;
            }
                //已经派单
                try{
                    order.setStatus(Order.OrderType.ALREADY);
                    String nameTel = object.getString("expressTel");
                    String expressName = object.getString("expressName");
                    if(!StringUtils.isBlank(nameTel)&&!StringUtils.isBlank(expressName)){
                        if (9==order.getCompanyId()) {
                            if (StringUtils.isNotBlank(nameTel)){
                                order.setExpressTel(nameTel.substring(nameTel.length()-11, nameTel.length()));
                                order.setExpressName(nameTel.substring(3,nameTel.length()-11));
                            }else if (StringUtils.isNotBlank(expressName)){
                                order.setExpressTel(expressName.substring(expressName.length()-11, expressName.length()));
                                order.setExpressName(expressName.substring(3,expressName.length()-11));
                            }
                        }else {
                            order.setExpressTel(nameTel);
                            order.setExpressName(expressName);
                        }
                    }
                    if(null != object.getString("expressNo") &&StringUtils.isNotBlank(object.getString("expressNo"))){
                        order.setExpressNo(object.getString("expressNo"));
                    }
                    if(null != object.getString("logisticsName")&&StringUtils.isNotBlank(object.getString("logisticsName"))){
                        order.setLogisticsName(object.getString("logisticsName"));
                    }
                    order.setReceiveTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getString("date")));
                    orderService.updateById(order);
                }catch (Exception e){
                    e.printStackTrace();
                }

            return null;
        }else if ("3".equals(orderStatus)){
            //订单完成
            try{
                if((Order.OrderType.ALREADY+"").equals(order.getStatus()+"")){
                    if(null != object.getString("expressNo") &&StringUtils.isNotBlank(object.getString("expressNo")+"")){
                        order.setExpressNo(object.getString("expressNo"));
                    }
                    if(null != object.getString("logisticsName")&&StringUtils.isNotBlank(object.getString("logisticsName")+"")){
                        order.setLogisticsName(object.getString("logisticsName"));
                    }
                    order.setGreenCount(Double.parseDouble(object.getString("expressAmount")));
                    order.setStatus(Order.OrderType.COMPLETE);
                    order.setExpressAmount(new BigDecimal(object.getString("expressAmount")));
                    order.setCompleteDate(new Date());
                    order.setAchPrice(new BigDecimal("0"));
                    orderService.updateById(order);
                    OrderItem orderItem = orderItemService.selectOne(new EntityWrapper<OrderItem>().eq("order_id", order.getId()));
                    OrderItemAch orderItemAch = new OrderItemAch();
                    orderItemAch.setOrderId(order.getId().intValue());
                    orderItemAch.setCategoryId(orderItem.getCategoryId());
                    orderItemAch.setCategoryName(orderItem.getCategoryName());
                    orderItemAch.setParentName(orderItem.getParentName());
                    orderItemAch.setParentId(orderItem.getParentId());
                    orderItemAch.setParentIds(orderItem.getParentIds());
                    orderItemAch.setAmount(Double.parseDouble(object.getString("expressAmount")));
                    orderItemAch.setUnit(orderItem.getUnit());
                    orderItemAch.setPrice(orderItem.getPrice());
                    orderItemAchService.insert(orderItemAch);
                    OrderPic orderPic = orderPicService.selectOne(new EntityWrapper<OrderPic>().eq("order_id", order.getId()));
                    OrderPicAch orderPicAch = new OrderPicAch();
                    orderPicAch.setOrderId(orderPic.getOrderId());
                    orderPicAch.setOrigPic(orderPic.getOrigPic());
                    orderPicAch.setPicUrl(orderPic.getPicUrl());
                    orderPicAch.setSmallPic(orderPic.getSmallPic());
                    orderPicAchService.insert(orderPicAch);
                    //给用户增加积分
                    orderService.updateMemberPoint(order.getMemberId(), order.getOrderNo(), order.getGreenCount(),"生活垃圾");
                    //给用户增加蚂蚁能量
                    OrderBean orderBean = orderService.myslOrderData(order.getId().toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }else if ("4".equals(orderStatus)){
            if((Order.OrderType.COMPLETE+"").equals(order.getStatus()+"")){
                return null;
            }
            //取消订单
            try{
                order.setStatus(Order.OrderType.REJECTED);
                order.setCancelReason(object.getString("remarks"));
                order.setCancelTime(new Date());
                orderService.updateById(order);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        return null;
    };

    /**
     * 预处理MNS收到的消息
     * @param body
     * @return
     */
    private Notification processMNSMessage(String body,String topicName) throws BusiException {
        Notification notification=null;
        try {

            //解析XML,不能使用json
            notification = XMLUtils.parse(body);
        }catch(Exception e){
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"解析JSON出错！可能不是MNS消息",
                    RocketMqConst.DINGDING_ERROR,body);
            throw new BusiException("解析JSON出错！可能不是MNS消息");
        }
        if(!notification.getTopicName().equals(topicName)){
            //说明不是这个TOPIC的Message，不需要处理，直接return
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"topicName不一致！",
                    RocketMqConst.DINGDING_ERROR,topicName);
            throw new BusiException("topicName不一致！");
        }
        String messageId=notification.getMessageId();
        if(StringUtils.isBlank(messageId)){
            //说明不是MNS通知的，直接return
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"messageId不能为空！",
                    RocketMqConst.DINGDING_ERROR,messageId);
            throw new BusiException("messageId不能为空！");
        }
        /*int count=processedMessageService.selectCount(new EntityWrapper<ProcessedMessage>().eq("message_id", messageId));
        if(count>1){
            //说明已经存在这个messageId了，直接return
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"messageId已被处理过了！！",
                    DingTalkConfig.DAQUN_WEBHOOK,messageId);
            throw new BusiException("messageId已被处理过了！");

        }*/

        try
        {
            RocketmqMessage rocketmqMessage = new RocketmqMessage();
            rocketmqMessage.setMessageId(messageId);
            rocketmqMessage.setMessage(notification.getMessage());
           // processedMessageService.insert(new ProcessedMessage(messageId,notification.getMessage(),new Date(),
           //         notification.getPublishTime()));
            rocketmqMessageService.insert(rocketmqMessage);
        }
        catch (Exception e)
        {
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"保存notification异常",
                    RocketMqConst.DINGDING_ERROR,e.toString());
            e.printStackTrace();
        }
        System.out.println("---------------------------"+notification.getPublishTime());
        return notification;
    }

    public static void main(String[] args) {
        HashMap<String,Object> param=new HashMap<>();
        param.put("orderStatus","3");
        param.put("orderNo","20190617160901505474");
        //param.put("expressName","接货中申武林13298688002");
        //param.put("expressTel","接货中申武林13298688002");
       // param.put("date", "2019-05-11 18:11:04");
        // param.put("expressNo","CVAV1129519734415190");
        param.put("expressAmount","12");
       // param.put("remarks","取消原因4");
      //  param.put("logisticsName","德邦");
//        param.put("orderNo","20190302135536851909");
//        param.put("orderType","废纺衣物");
//        param.put("orderAmount","6.5");
//        param.put("userName","龙建");
//        param.put("userTel", "18555696367");
//        param.put("userAddress","上海市浦东新区洲海路100号10栋101");
//        param.put("arrivalTime","2019-02-26 am");
//        param.put("isCancel","Y");
//        param.put("cancelReason","取消原因");
String tel = "接货中何海平18046748800";
        System.out.println(tel.substring(3,tel.length()-11));
        System.out.println(tel.substring(tel.length()-11, tel.length()));

        System.out.println(VoucherType.discount.getNameCN());
        RocketMqConst.sendDeliveryOrder(JSON.toJSONString(param),"ReturnOrder");

        List<Order> list = new ArrayList<>();
        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        Order order4 = new Order();
        Order order5 = new Order();
        order1.setLinkMan("王灿1");
        order2.setLinkMan("王灿2");
        order3.setLinkMan("王灿3");
        order4.setLinkMan("王灿5");
        order4.setIsScan("99");
        order5.setLinkMan("王灿5");
        order5.setIsScan("7");
        list.add(order1);
        list.add(order2);
        list.add(order3);
        list.add(order4);
        list.add(order5);
      //  List<Order> collect = list.stream().filter(order -> order.getLinkMan() == "王灿5").collect(toList());

//        System.out.println(LocalTime.now());
//        System.out.println(LocalDate.now());
//        System.out.println(LocalDateTime.now().toString());


        //sendTest();
    }
    private static void xmlTest() {
        String xml="<Notification xmlns=\"http://mns.aliyuncs.com/doc/v1/\">\n" +
                "  <TopicOwner>1804870195031869</TopicOwner>\n" +
                "  <TopicName>JQCRM</TopicName>\n" +
                "  <Subscriber>1804870195031869</Subscriber>\n" +
                "  <SubscriptionName>sub-http-jqcrm</SubscriptionName>\n" +
                "  <MessageId>FBA0438BE5D9BBD8-2-15FDC6B733F-2000001C7</MessageId>\n" +
                "  <MessageMD5>CC3DC57843466ED85CEECA88D7A326C0</MessageMD5>\n" +
                "  <Message>{\"date\":\"2017-11-21\",\"member_card\":\"700003422\",\"origin\":\"POS\",\"points\":\"9\",\"remain\":\"8682\",\"transaction_no\":\"20171121VR01000NVO\"}</Message>\n" +
                "  <PublishTime>1511231550271</PublishTime>\n" +
                "  <SigningCertURL>https://mnstest.oss-cn-hangzhou.aliyuncs.com/x509_public_certificate.pem</SigningCertURL>\n" +
                "</Notification>";

        Notification notification = XMLUtils.parse(xml);
        System.out.println(notification.getMessageId());
        System.out.println(notification.getMessage());
    }


}
