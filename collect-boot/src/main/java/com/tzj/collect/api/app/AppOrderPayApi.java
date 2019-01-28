package com.tzj.collect.api.app;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.tzj.collect.api.app.param.OrderPayParam;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.service.MemberService;
import com.tzj.collect.service.OrderService;
import com.tzj.collect.service.PaymentService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.doc.annotation.ApiDocMethod;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static com.tzj.collect.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

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

    @Api(name = "app.order.pay", version = "1.0")
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
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

        Subject subject= ApiContext.getSubject();
        //接口里面获取  Recyclers 的例子
        Recyclers recyclers = (Recyclers) subject.getUser();

        Payment payment=paymentService.selectByOrderSn(order.getOrderNo());
        Member member = memberService.selectById(order.getMemberId());
        if(payment==null){
            payment=new Payment();
            payment.setOrderSn(order.getOrderNo());
            payment.setPrice(orderPayParam.getPrice());
            payment.setRecyclersId(recyclers.getId());
            payment.setAliUserId(member.getAliUserId());
            payment.setStatus(Payment.STATUS_UNPAY);
            paymentService.insert(payment);
        }else{
            //判断订单是否支付
            if(payment.getStatus()==1){
                return "订单已支付";
            }
            //判断回收人员是否支付成功
            if(!StringUtils.isBlank(payment.getTradeNo())){
                AlipayTradeQueryResponse aliPayment = paymentService.getAliPayment(payment.getTradeNo());
                if ("TRADE_SUCCESS".equals(aliPayment.getTradeStatus())){
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
        return paymentService.genalPay(payment);
    }
}
