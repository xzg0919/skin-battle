package com.tzj.collect.api.app;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.commom.constant.WXConst;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.app.OrderPayParam;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 订单支付接口
 */
@ApiService
public class AppOrderPayApi {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;
    @Resource(name = "mqtt4PushOrder")
    private MqttClient mqtt4PushOrder;
    @Autowired
    private RecyclersService recyclersService;
    @Autowired
    private VoucherMemberService voucherMemberService;

    @Autowired
    private WXPayService wxPayService;

    @Api(name = "app.order.pay", version = "1.0")
    public String orderPay(OrderPayParam orderPayParam) {

        System.out.println("传入的订单Id：" + orderPayParam.getOrderId() + "++传入的价格是：" + orderPayParam.getPrice() + "优惠券Id：" + orderPayParam.getVoucherId());
        if (orderPayParam.getPrice().compareTo(BigDecimal.ZERO) == 0) {
            throw new ApiException("不能支付0元");
        }

        Order order = orderService.selectById(orderPayParam.getOrderId());
        if (order == null) {
            throw new ApiException("未找到Order信息！id:" + orderPayParam.getOrderId());
        }
        Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());

        Payment payment = paymentService.selectByOrderSn(order.getOrderNo());

        if (payment == null) {
            payment = new Payment();
            payment.setOrderSn(order.getOrderNo());
            payment.setPrice(orderPayParam.getPrice());
            payment.setRecyclersId(recyclers.getId());
            payment.setAliUserId(order.getAliUserId());
            payment.setStatus(Payment.STATUS_UNPAY);
            paymentService.insert(payment);
            order.setAchPrice(orderPayParam.getPrice());
            order.setDiscountPrice(orderPayParam.getPrice());
            orderService.updateById(order);
        } else {
            //判断订单是否支付
            if (payment.getStatus() == 1) {
                //修改订单状态
                OrderBean orderBean = new OrderBean();
                orderBean.setId(order.getId().intValue());
                orderBean.setStatus("3");
                orderBean.setAmount(order.getGreenCount());
                orderService.modifyOrderSta(orderBean, mqtt4PushOrder);
                return "订单已支付";
            }
            //判断回收人员是否支付成功
            if (!StringUtils.isBlank(payment.getTradeNo())) {
                AlipayTradeQueryResponse aliPayment = paymentService.getAliPayment(payment.getTradeNo());
                if ("TRADE_SUCCESS".equals(aliPayment.getTradeStatus())) {
                    //修改订单状态
                    OrderBean orderBean = new OrderBean();
                    orderBean.setId(order.getId().intValue());
                    orderBean.setStatus("3");
                    orderBean.setAmount(order.getGreenCount());
                    orderService.modifyOrderSta(orderBean, mqtt4PushOrder);
                    return "订单已支付";
                }
            }
            BigDecimal a = orderPayParam.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
            BigDecimal b = payment.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
            if (!a.equals(b)) {
                payment.setOrderSn(order.getOrderNo());
                payment.setPrice(orderPayParam.getPrice());
                payment.setRecyclersId(recyclers.getId());
                payment.setAliUserId(order.getAliUserId());
                payment.setStatus(Payment.STATUS_UNPAY);
                paymentService.updateById(payment);
                order.setAchPrice(orderPayParam.getPrice());
                order.setDiscountPrice(orderPayParam.getPrice());
                orderService.updateById(order);
            }
        }
        if ((order.getTitle() + "").equals(Order.TitleType.BIGTHING + "")) {
            if (StringUtils.isNotBlank(orderPayParam.getVoucherId())) {
                return voucherMemberService.updateOrderNo(orderPayParam.getPrice(), orderPayParam.getOrderId(), orderPayParam.getVoucherId(), payment);
            } else {
                return paymentService.genalPayXcx(payment, order);
            }
        } else {
            return paymentService.genalPay(payment, order);
        }
    }

    /**
     * 新的支付接口
     * 可支持同一订单无限制支付
     *
     * @param orderPayParam
     * @return
     */
    @Api(name = "app.order.tradePay", version = "1.0")
    @SignIgnore
    public String orderTradePay(OrderPayParam orderPayParam) {
        if (orderPayParam.getPrice().compareTo(BigDecimal.ZERO) == 0) {
            throw new ApiException("不能支付0元");
        }

        Order order = orderService.selectById(orderPayParam.getOrderId());
        if (order == null) {
            throw new ApiException("未找到Order信息！id:" + orderPayParam.getOrderId());
        } else if ("2".equals(order.getOrderFrom())) {
            //如果是闲鱼订单，直接完成
            OrderBean orderBean = new OrderBean();
            orderBean.setStatus("3");
            orderBean.setId(order.getId().intValue());
            orderBean.setAchPrice(order.getAchPrice().toString());
            orderBean.setAmount(order.getGreenCount());
            orderService.modifyOrderSta(orderBean, mqtt4PushOrder);
            return "确认完成";
        }
        Payment payment = paymentService.selectPayByOrderSn(order.getOrderNo());
        if (null != payment) {
            return "该订单已被支付，请勿重复支付";
        } else {
            if ((order.getTitle() + "").equals(Order.TitleType.BIGTHING + "")) {
                //查询最近一分钟内未支付成功的订单
                payment = paymentService.selectPayOneMinByOrderSn(order.getOrderNo());
                if (null != payment) {
                    return payment.getTradeNo();
                }
            }
            payment = new Payment();
        }
        Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());

        payment.setOrderSn(order.getOrderNo());
        payment.setPrice(orderPayParam.getPrice());
        payment.setRecyclersId(recyclers.getId());
        payment.setAliUserId(order.getAliUserId());
        payment.setStatus(Payment.STATUS_UNPAY);
        paymentService.insert(payment);
        if ((order.getTitle() + "").equals(Order.TitleType.BIGTHING + "")) {
            if (StringUtils.isNotBlank(orderPayParam.getVoucherId())) {
                return voucherMemberService.updateOrderNo(orderPayParam.getPrice(), orderPayParam.getOrderId(), orderPayParam.getVoucherId(), payment);
            } else {
                return paymentService.genalPayXcx(payment, order);
            }
        } else {
            return paymentService.genalPay(payment, order);
        }
    }

    /**
     * 新的支付接口
     * 可支持同一订单无限制支付
     *
     * @param orderPayParam
     * @return
     */
    @Api(name = "app.order.tradeClose", version = "1.0")
    public String paymentCloseByTradeNo(OrderPayParam orderPayParam) {
        paymentService.paymentCloseByTradeNo(orderPayParam.getOutTradeNo());
        return "操作成功";
    }

    /**
     * 支付成功状态更改
     *
     * @param orderPayParam
     * @return
     */
    @Api(name = "app.order.successPay", version = "1.0")
    public String paymentSuccessPay(OrderPayParam orderPayParam) {
        Payment payment = paymentService.selectByOutTradeNo(orderPayParam.getOutTradeNo());
        if (payment == null) {
            throw new ApiException("未找到相应的支付信息");
        }
        if (0 == payment.getStatus()) {
            payment.setStatus(Payment.STATUS_PAYED);
        }
        paymentService.updateById(payment);
        return "success";
    }




    /**
     * app微信支付预支付
     *
     * @param orderPayParam
     * @return
     */
    @Api(name = "app.order.wxPrePay", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object wxPay(OrderPayParam orderPayParam) {
        Order order = orderService.getByOrderNo(orderPayParam.getOrderCode());
        if (order == null || !order.getDelFlag().equals("0") || order.getStatus().equals(Order.OrderType.COMPLETE) || order.getStatus().equals(Order.OrderType.CANCEL)) {
            throw new ApiException("订单错误");
        }
        return   JSONObject.toJSON(wxPayService.prePay(orderPayParam.getOpenId(), order.getOrderNo(),
                WXConst.FROM_APP, orderPayParam.getTotalFee(), orderPayParam.getPayBody(), PrepayOrder.COLLECT));

    }
}
