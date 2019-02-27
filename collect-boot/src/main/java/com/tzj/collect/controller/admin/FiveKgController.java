package com.tzj.collect.controller.admin;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.auth.ServiceCredentials;
import com.aliyun.mns.common.http.ServiceClient;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.constant.RocketMqConst;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderItem;
import com.tzj.collect.entity.OrderItemAch;
import com.tzj.collect.entity.RocketmqMessage;
import com.tzj.collect.service.OrderItemAchService;
import com.tzj.collect.service.OrderItemService;
import com.tzj.collect.service.OrderService;
import com.tzj.collect.service.RocketmqMessageService;
import com.tzj.module.common.aliyun.mns.Notification;
import com.tzj.module.common.aliyun.mns.XMLUtils;
import com.tzj.module.common.exception.BusiException;
import com.tzj.module.common.notify.dingtalk.DingTalkConfig;
import com.tzj.module.common.notify.dingtalk.DingTalkNotify;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
            //已经派单
            try{
                order.setStatus(Order.OrderType.ALREADY);
                order.setExpressName(object.getString("expressName"));
                if (StringUtils.isNotBlank(object.getString("expressTel"))){
                    order.setExpressTel(object.getString("expressTel"));
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
                order.setStatus(Order.OrderType.COMPLETE);
                order.setExpressNo(object.getString("expressNo"));
                order.setExpressAmount(new BigDecimal(object.getString("expressAmount")));
                order.setCompleteDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getString("date")));
                orderService.updateById(order);
                OrderItem orderItem = orderItemService.selectOne(new EntityWrapper<OrderItem>().eq("order_id", order.getId()));
                OrderItemAch orderItemAch = new OrderItemAch();
                orderItemAch.setOrderId(order.getId().intValue());
                orderItemAch.setCategoryId(orderItem.getCategoryId());
                orderItemAch.setCategoryName(orderItem.getCategoryName());
                orderItemAch.setParentName(orderItem.getParentName());
                orderItemAch.setParentId(orderItem.getParentId());
                orderItemAch.setParentIds(orderItem.getParentIds());
                orderItemAch.setAmount(orderItem.getAmount());
                orderItemAch.setUnit(orderItem.getUnit());
                orderItemAch.setPrice(orderItem.getPrice());
                orderItemAchService.insert(orderItemAch);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }else if ("4".equals(orderStatus)){
            //取消订单
            try{
                order.setStatus(Order.OrderType.REJECTED);
                order.setRemarks(object.getString("remarks"));
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
        param.put("orderNo","20190227112534438185");
        param.put("expressName","岳阳");
        param.put("expressTel","18375333333");
        param.put("date", "2019-02-27 14:27:01");
        param.put("expressNo","20187974123466789");
        param.put("expressAmount","4.5");
        param.put("remarks","取消原因4");
//        param.put("orderNo","20190225164500925088");
//        param.put("orderType","废纺衣物");
//        param.put("orderAmount","6.5");
//        param.put("userName","龙建");
//        param.put("userTel", "18555696367");
//        param.put("userAddress","上海市浦东新区洲海路100号10栋101");
//        param.put("arrivalTime","2019-02-26 am");
        RocketMqConst.sendDeliveryOrder(JSON.toJSONString(param),RocketMqConst.TOPIC_NAME_RETURN_ORDER);
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
