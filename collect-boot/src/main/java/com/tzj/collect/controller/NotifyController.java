package com.tzj.collect.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.constant.Const;
import com.tzj.collect.common.thread.NewThreadPoorExcutor;
import com.tzj.collect.common.thread.sendGreenOrderThread;
import com.tzj.collect.common.utils.MiniTemplatemessageUtil;
import com.tzj.collect.common.utils.PushUtils;
import com.tzj.collect.common.utils.VoucherUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tzj.collect.api.common.constant.Const.ALI_APPID;
import static com.tzj.collect.api.common.constant.Const.ALI_PUBLIC_KEY;

/**
 * 支付通知
 */
@Controller
@RequestMapping(value = "/notify")
public class NotifyController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private EnterpriseCodeService enterpriseCodeService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private OrderItemAchService orderItemAchService;
	@Autowired
    private AsyncService asyncService;
    @Autowired
    private RecyclersService recyclersService;
    @Autowired
    private VoucherMemberService voucherMemberService;


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
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, ALI_PUBLIC_KEY, "UTF-8", "RSA2");
            if (flag) {
                //验签成功后
                //按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
                String orderSN = params.get("out_trade_no");
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

                Payment payment=paymentService.selectByOrderSn(orderSN);
                if (payment == null) {
                    //未找到相对应的订单
                    return "failure";
                }

//                if (payment.getPrice().compareTo(new BigDecimal(totalAmount)) != 0) {
//                    //金额不匹配
//                    return "failure";
//                }
                Order order = null;
                VoucherMember voucherMember = null;

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
                    //給用戶轉賬
                    paymentService.transfer(payment);

                    //根據order_no查询相关订单
                    order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", orderSN).eq("del_flag", 0));

                    if(("1".equals(order.getIsMysl())&&(order.getStatus()+"").equals(Order.OrderType.ALREADY+""))||order.getIsScan().equals("1")){
                        //给用户增加蚂蚁能量
                        OrderBean orderBean = orderService.myslOrderData(order.getId().toString());
					}
                    Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
                    if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
                        PushUtils.getAcsResponse(recyclers.getTel(),"3",order.getTitle().getValue()+"");
                    }
					try {
                        if (order.getAddress().startsWith("上海市")&&(Order.TitleType.HOUSEHOLD+"").equals(order.getTitle()+"")){
                            NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new sendGreenOrderThread(orderService,areaService,orderItemAchService,order.getId().intValue())));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(null != order&&order.getIsScan().equals("0")){
                        //修改订单状态
                        OrderBean orderBean = new OrderBean();
                        orderBean.setId(order.getId().intValue());
                        orderBean.setStatus("3");
                        orderBean.setAmount(order.getGreenCount());
                        orderBean.setAchPrice(totalAmount);
                        orderService.modifyOrderByPayment(orderBean);
                        //判断是否有券码完成转账
                        if(!StringUtils.isBlank(order.getEnterpriseCode())){
                            EnterpriseCode enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", order.getEnterpriseCode()).eq("del_flag", 0).eq("is_use",1));
                            //判断券码是否存在并且未使用
                            if(null!=enterpriseCode){
                                //储存转账信息
                                Payment payments = new Payment();
                                payments.setAliUserId(order.getAliUserId());
                                payments.setTradeNo("1");
                                payments.setPrice(enterpriseCode.getPrice());
                                payments.setRecyclersId(order.getRecyclerId().longValue());
                                payments.setOrderSn(order.getOrderNo());
                                paymentService.insert(payments);
                                //给用户转账
                                paymentService.transfer(payments);
                                enterpriseCode.setReceiveDate(new Date());
                                enterpriseCode.setIsUse("2");
                                enterpriseCodeService.updateById(enterpriseCode);
                            }
                        }
                    }
                }else if (tradeStatus.equalsIgnoreCase("TRADE_CLOSED")){
                    voucherMember = voucherMemberService.selectOne(new EntityWrapper<VoucherMember>().eq("order_no", order.getOrderNo()).eq("ali_user_id", order.getAliUserId()));
                    if(null != voucherMember){
                        voucherMemberService.updateVoucherCreate(voucherMember.getId());
                    }
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
