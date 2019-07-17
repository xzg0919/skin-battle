package com.tzj.collect.api.app;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.app.OrderPayParam;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.core.service.PaymentService;
import com.tzj.collect.core.service.RecyclersService;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.entity.Recyclers;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.doc.annotation.ApiDocMethod;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Autowired
    private MemberService memberService;
    @Autowired
    private RecyclersService recyclersService;

    @Api(name = "app.order.pay", version = "1.0")
    @SignIgnore
    @ApiDocMethod(description="订单支付宝支付",remark = "订单支付宝支付")
    public String orderPay(OrderPayParam orderPayParam) {

        System.out.println("传入的订单Id："+orderPayParam.getOrderId()+"++传入的价格是："+orderPayParam.getPrice());
        if(orderPayParam.getPrice().compareTo(BigDecimal.ZERO)==0){
            throw new ApiException("不能支付0元");
        }

        Order order=orderService.selectById(orderPayParam.getOrderId());
        if(order==null){
            throw new ApiException("未找到Order信息！id:"+orderPayParam.getOrderId());
        }
        Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());

        Payment payment=paymentService.selectByOrderSn(order.getOrderNo());

        if(payment==null){
            payment=new Payment();
            payment.setOrderSn(order.getOrderNo());
            payment.setPrice(orderPayParam.getPrice());
            payment.setRecyclersId(recyclers.getId());
            payment.setAliUserId(order.getAliUserId());
            payment.setStatus(Payment.STATUS_UNPAY);
            paymentService.insert(payment);
        }else{
            //判断订单是否支付
            if(payment.getStatus()==1){
                //修改订单状态
                OrderBean orderBean = new OrderBean();
                orderBean.setId(order.getId().intValue());
                orderBean.setStatus("3");
                orderBean.setAmount(order.getGreenCount());
                orderService.modifyOrderSta(orderBean);
                return "订单已支付";
            }
            //判断回收人员是否支付成功
            if(!StringUtils.isBlank(payment.getTradeNo())){
                AlipayTradeQueryResponse aliPayment = paymentService.getAliPayment(payment.getTradeNo());
                if ("TRADE_SUCCESS".equals(aliPayment.getTradeStatus())){
                    //修改订单状态
                    OrderBean orderBean = new OrderBean();
                    orderBean.setId(order.getId().intValue());
                    orderBean.setStatus("3");
                    orderBean.setAmount(order.getGreenCount());
                    orderService.modifyOrderSta(orderBean);
                    return "订单已支付";
                }
            }
            BigDecimal a =  orderPayParam.getPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal b = payment.getPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
            if (!a.equals(b)){
                payment.setOrderSn(order.getOrderNo());
                payment.setPrice(orderPayParam.getPrice());
                payment.setRecyclersId(recyclers.getId());
                payment.setAliUserId(order.getAliUserId());
                payment.setStatus(Payment.STATUS_UNPAY);
                paymentService.updateById(payment);
            }
        }
        if ((order.getTitle()+"").equals(Order.TitleType.BIGTHING+"")){
            return paymentService.genalPayXcx(payment);
        }else{
            return paymentService.genalPay(payment);
        }
    }
}
