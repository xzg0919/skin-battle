package com.tzj.collect.controller.admin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.mq.RocketMqConst;
import com.tzj.collect.common.constant.MiniTemplatemessageUtil;
import com.tzj.collect.config.ApplicationInit;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.common.aliyun.mns.Notification;
import com.tzj.module.common.aliyun.mns.XMLUtils;
import com.tzj.module.common.exception.BusiException;
import com.tzj.collect.common.notify.DingTalkNotify;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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
    @Autowired
    private ApplicationInit applicationInit;
    @Autowired
    private AsyncService asyncService;

    @Autowired
    PaymentService paymentService;

    @RequestMapping(value = "/order/update", method = RequestMethod.POST)
    public String orderUpdate(@RequestBody String body, HttpServletResponse response) {
        String rcketMqConst = RocketMqConst.TOPIC_NAME_RETURN_ORDER_TEST;
        if ("true".equals(applicationInit.getIsMysl())) {
            rcketMqConst = RocketMqConst.TOPIC_NAME_RETURN_ORDER;
        }
        System.out.println("收到的body-----" + body);
        Notification notification = null;
        try {
            notification = processMNSMessage(body, rcketMqConst);
        } catch (Exception e) {
            //说明接收到的消息有问题，直接丢弃掉
            System.out.println("message有问题:" + e.getMessage());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName(), "message有问题:",
                    RocketMqConst.DINGDING_ERROR, e.toString() + body);
            return null;
        }
        String message = notification.getMessage();
        JSONObject object = JSON.parseObject(message);
        String orderStatus = object.getString("orderStatus");
        String orderNo = object.getString("orderNo");
        if (StringUtils.isBlank(orderStatus) || StringUtils.isBlank(orderNo)) {
            return null;
        }
        Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", orderNo).eq("del_flag", 0));
        if (null == order) {
            throw new ApiException("订单不存在，order_no是：" + orderNo);
        }
        if ("2".equals(orderStatus)) {
            if ((Order.OrderType.COMPLETE + "").equals(order.getStatus() + "") || (Order.OrderType.CANCEL + "").equals(order.getStatus() + "") || (Order.OrderType.REJECTED + "").equals(order.getStatus() + "")) {
                return null;
            }
            //已经派单
            try {
                order.setStatus(Order.OrderType.ALREADY);
                String nameTel = object.getString("expressTel");
                String expressName = object.getString("expressName");
                if (!StringUtils.isBlank(nameTel) && !StringUtils.isBlank(expressName)) {
                    if (9 == order.getCompanyId()) {
                        if (StringUtils.isNotBlank(nameTel)) {
                            order.setExpressTel(nameTel.substring(nameTel.length() - 11, nameTel.length()));
                            order.setExpressName(nameTel.substring(3, nameTel.length() - 11));
                        } else if (StringUtils.isNotBlank(expressName)) {
                            order.setExpressTel(expressName.substring(expressName.length() - 11, expressName.length()));
                            order.setExpressName(expressName.substring(3, expressName.length() - 11));
                        }
                    } else {
                        order.setExpressTel(nameTel);
                        order.setExpressName(expressName);
                    }
                }
                if (null != object.getString("expressNo") && StringUtils.isNotBlank(object.getString("expressNo"))) {
                    order.setExpressNo(object.getString("expressNo"));
                }
                if (null != object.getString("logisticsName") && StringUtils.isNotBlank(object.getString("logisticsName"))) {
                    order.setLogisticsName(object.getString("logisticsName"));
                }
                order.setReceiveTime(new Date());
                orderService.updateById(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if ((Order.TitleType.BIGTHING + "").equals(order.getTitle() + "")) {
                    asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "已接单", "大件回收");
                } else if ((Order.TitleType.DIGITAL + "").equals(order.getTitle() + "")) {
                    asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "已接单", "家电回收");
                } else {
                    asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "已接单", "生活垃圾");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else if ("3".equals(orderStatus)) {
            //订单完成
            try {
                if ((Order.OrderType.ALREADY + "").equals(order.getStatus() + "")) {
                    if (null != object.getString("expressNo") && StringUtils.isNotBlank(object.getString("expressNo") + "")) {
                        order.setExpressNo(object.getString("expressNo"));
                    }
                    if (null != object.getString("logisticsName") && StringUtils.isNotBlank(object.getString("logisticsName") + "")) {
                        order.setLogisticsName(object.getString("logisticsName"));
                    }
                    order.setGreenCount(Double.parseDouble(object.getString("expressAmount")) * 2);
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
                    orderService.updateMemberPoint(order.getAliUserId(), order.getOrderNo(), order.getGreenCount(), "生活垃圾");
                    //给用户增加蚂蚁能量
                    OrderBean orderBean = orderService.myslOrderData(order.getId().toString());
                    //活动  完成订单大于五公斤发送红包0.1元
                    /*if ( order.getExpressAmount().compareTo(new BigDecimal("5.0")) == 1
                     || order.getExpressAmount().compareTo(new BigDecimal("5.0")) == 0) {
                        paymentService.transfer(order.getAliUserId(), "0.1", order.getOrderNo());
                    }*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if ((Order.TitleType.BIGTHING + "").equals(order.getTitle() + "")) {
                    asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "已完成", "大件回收");
                } else if ((Order.TitleType.DIGITAL + "").equals(order.getTitle() + "")) {
                    asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "已完成", "家电回收");
                } else {
                    asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "已完成", "生活垃圾");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else if ("4".equals(orderStatus)) {
            if ((Order.OrderType.COMPLETE + "").equals(order.getStatus() + "")) {
                return null;
            }
            //取消订单
            try {
                order.setStatus(Order.OrderType.REJECTED);
                order.setCancelReason(object.getString("remarks"));
                order.setCancelTime(new Date());
                orderService.updateById(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if ((Order.TitleType.BIGTHING + "").equals(order.getTitle() + "")) {
                    asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "已取消", "大件回收");
                } else if ((Order.TitleType.DIGITAL + "").equals(order.getTitle() + "")) {
                    asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "已取消", "家电回收");
                } else {
                    asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "已取消", "生活垃圾");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    ;

    /**
     * 预处理MNS收到的消息
     *
     * @param body
     * @return
     */
    private Notification processMNSMessage(String body, String topicName) throws BusiException {
        Notification notification = null;
        try {

            //解析XML,不能使用json
            notification = XMLUtils.parse(body);
        } catch (Exception e) {
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName(), "解析JSON出错！可能不是MNS消息",
                    RocketMqConst.DINGDING_ERROR, body);
            throw new BusiException("解析JSON出错！可能不是MNS消息");
        }
        if (!notification.getTopicName().equals(topicName)) {
            //说明不是这个TOPIC的Message，不需要处理，直接return
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName(), "topicName不一致！",
                    RocketMqConst.DINGDING_ERROR, topicName);
            throw new BusiException("topicName不一致！");
        }
        String messageId = notification.getMessageId();
        if (StringUtils.isBlank(messageId)) {
            //说明不是MNS通知的，直接return
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName(), "messageId不能为空！",
                    RocketMqConst.DINGDING_ERROR, messageId);
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

        try {
            RocketmqMessage rocketmqMessage = new RocketmqMessage();
            rocketmqMessage.setMessageId(messageId);
            rocketmqMessage.setMessage(notification.getMessage());
            // processedMessageService.insert(new ProcessedMessage(messageId,notification.getMessage(),new Date(),
            //         notification.getPublishTime()));
            rocketmqMessageService.insert(rocketmqMessage);
        } catch (Exception e) {
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName(), "保存notification异常",
                    RocketMqConst.DINGDING_ERROR, e.toString());
            e.printStackTrace();
        }
        System.out.println("---------------------------" + notification.getPublishTime());
        return notification;
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal("5.1").compareTo(new BigDecimal("5.0")) == 1);
        System.out.println(new BigDecimal("5").compareTo(new BigDecimal("5.0")) == 0);
    }

    private static void xmlTest() {
        System.out.println(new BigDecimal("5.0").compareTo(new BigDecimal("5.0")) == 1);
    }


}
