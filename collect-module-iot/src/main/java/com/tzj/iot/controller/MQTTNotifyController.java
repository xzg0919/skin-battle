package com.tzj.iot.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.push.PushUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.core.thread.NewThreadPoorExcutor;
import com.tzj.collect.core.thread.sendGreenOrderThread;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.entity.VoucherMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.tzj.collect.common.constant.Const.ALI_APPID;
import static com.tzj.collect.common.constant.Const.ALI_PUBLIC_KEY;

/**
 * 支付通知
 */
@Controller
@RequestMapping(value = "/notify")
public class MQTTNotifyController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;
    /**
     * 支付宝支付通知
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/alipay")
    public @ResponseBody
    String aliPayNotify(HttpServletRequest request, ModelMap model) {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        System.out.println("支付异部通知-------------------------------------------------------"+JSON.toJSONString(params));
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, ALI_PUBLIC_KEY, "UTF-8", "RSA2");
            if (flag) {
                //验签成功后
                //按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
                String outTradeNo = params.get("out_trade_no");
                String appId = params.get("app_id");
                String tradeStatus = params.get("trade_status");
                String totalAmount = params.get("total_amount");
                //1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
                //2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额
                //4、验证app_id是否为该商户本身
                //TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功

                if (!appId.equals(ALI_APPID)) {
                    return "failure";
                }

                Payment payment=paymentService.selectByOutTradeNo(outTradeNo);
                if (payment == null) {
                    //未找到相对应的订单
                    return "failure";
                }
//                if (payment.getPrice().compareTo(new BigDecimal(totalAmount)) != 0) {
//                    //金额不匹配
//                    return "failure";
//                }
                //根據order_no查询相关订单
                Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", payment.getOrderSn()).eq("del_flag", 0));

                if (tradeStatus.equalsIgnoreCase("TRADE_SUCCESS") ||
                        tradeStatus.equalsIgnoreCase("TRADE_FINISHED")) {
                    //说明交易完成，记录下一些交易的信息

                    payment.setNotifyUrl(AlipaySignature.getSignCheckContentV1(params));

                    payment.setTradeNo(params.get("trade_no"));
                    payment.setNotifyTime(params.get("notify_time"));
                    payment.setBuyerId(params.get("buyer_id"));
                    payment.setSellerId(params.get("seller_id"));
                    payment.setStatus(Payment.STATUS_PAYED);
                    payment.setBuyerLogonId(params.get("buyer_logon_id"));

                    paymentService.insertOrUpdate(payment);
                    payment.setTotalAmount(totalAmount);
                    paymentService.transfer(payment);
                    if(("1".equals(order.getIsMysl())&&(order.getStatus()+"").equals(Order.OrderType.ALREADY+""))||order.getIsScan().equals("1")){
                        //给用户增加蚂蚁能量
                        OrderBean orderBean = orderService.myslOrderData(order.getId().toString());
					}
//                    Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
//                    if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
//                        PushUtils.getAcsResponse(recyclers.getTel(),"3",order.getTitle().getValue()+"");
//                    }
                }else if (tradeStatus.equalsIgnoreCase("TRADE_CLOSED")){
//                    if(null != voucherMember){
//                        voucherMemberService.updateVoucherCreate(voucherMember.getId());
//                    }
                    return "failure";
                }

            } else {
                return "failure";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        return "success";
    }
}
