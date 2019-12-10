package com.tzj.iot.api.app;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.ZolozAuthenticationCustomerFtokenQueryResponse;
import com.tzj.collect.common.util.RecyclersUtils;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.param.app.OrderPayParam;
import com.tzj.collect.core.param.iot.SmilePayBean;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.entity.Recyclers;
import com.tzj.iot.api.ali.TokenApi;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.doc.annotation.ApiDoc;
import com.tzj.module.easyopen.doc.annotation.ApiDocMethod;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * @author sgmark
 * @create 2019-11-26 11:17
 **/
@ApiService
@ApiDoc(value = "APP iot 设备端app模块",appModule = "ali")
public class IotAppApi {

    @Resource
    private OrderService orderService;
    @Resource
    private PaymentService paymentService;
    @Resource
    private RecyclersService recyclersService;

    @Api(name = "app.order.tradePay", version = "1.0")
    @ApiDocMethod(description="订单支付宝支付",remark = "订单支付宝支付")
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public String orderTradePay(OrderPayParam orderPayParam) {
        if(orderPayParam.getPrice().compareTo(BigDecimal.ZERO)==0){
            throw new ApiException("不能支付0元");
        }

        Order order=orderService.selectById(orderPayParam.getOrderId());
        if(order==null){
            throw new ApiException("未找到Order信息！id:"+orderPayParam.getOrderId());
        }

        Payment payment=paymentService.selectPayByOrderSn(order.getOrderNo());
        if (null != payment){
            return "该订单已被支付，请勿重复支付";
        }else {
            payment=new Payment();
        }
        Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());

        payment.setOrderSn(order.getOrderNo());
        payment.setPrice(order.getPrice());
        payment.setRecyclersId(recyclers.getId());
        payment.setAliUserId(order.getAliUserId());
        payment.setStatus(Payment.STATUS_UNPAY);
        paymentService.insert(payment);
        return paymentService.iotPay(order.getIotEquipmentCode(), order.getOrderNo(), order.getPrice()+"", payment.getOutTradeNo());
    }

}
