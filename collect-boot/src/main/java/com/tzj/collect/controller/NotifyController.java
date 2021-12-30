package com.tzj.collect.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.tzj.collect.api.commom.constant.WXConst;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.core.thread.NewThreadPoorExcutor;
import com.tzj.collect.core.thread.sendGreenOrderThread;
import com.tzj.collect.common.push.PushUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.common.utils.StringUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.zip.ZipFile;

import static com.tzj.collect.common.constant.Const.ALI_APPID;
import static com.tzj.collect.common.constant.Const.ALI_PUBLIC_KEY;

/**
 * 支付通知
 */
@Controller
@RequestMapping(value = "/notify")
public class NotifyController {
    protected final static Logger logger = LoggerFactory.getLogger(NotifyController.class);

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private OrderItemAchService orderItemAchService;
    @Autowired
    private RecyclersService recyclersService;
    @Autowired
    private VoucherMemberService voucherMemberService;
    @Autowired
    private VoucherAliService voucherAliService;
    @Resource(name = "wxPayService")
    WxPayService wxPayService;
    @Autowired
    PrepayOrderService prepayOrderService;
    @Autowired
    WxPaymentService wxPaymentService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    JedisPool jedisPool;


    /**
     * 支付宝支付通知
     *
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
        System.out.println("支付异部通知-------------------------------------------------------" + JSON.toJSONString(params));
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

                Payment payment = paymentService.selectByOutTradeNo(outTradeNo);
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
                //如果订单完成了就直接返回成功
                if (Order.OrderType.COMPLETE.equals(order.getStatus())) {
                    return "success";
                }
                //根据订单号查询绑定券的信息
                VoucherMember voucherMember = voucherMemberService.selectOne(new EntityWrapper<VoucherMember>().eq("order_no", order.getOrderNo()).eq("ali_user_id", order.getAliUserId()));

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
                    payment.setVoucherMember(voucherMember);
                    //給用戶轉賬
                    if (!(Order.TitleType.BIGTHING + "").equals(order.getTitle() + "")) {
                        if (null != voucherMember) {
                            //限制区域内的不能使用优惠券
                            String voucherLimitAddressId = (String) redisUtil.get("voucherLimitAddressId");
                            if (voucherLimitAddressId != null &&
                                    !voucherLimitAddressId.contains(order.getStreetId().toString())
                                    && !voucherLimitAddressId.contains(order.getAreaId().toString())
                            ) {
                                //如果不是大件并且有优惠券，则计算使用该券后的价格给用户进行转账
                                BigDecimal discountPrice = voucherAliService.getDiscountPriceByVoucherId(order.getAchPrice(), voucherMember.getId().toString());
                                payment.setDiscountPrice(discountPrice);
                                payment.setTransferPrice(discountPrice);
                            } else {
                                payment.setDiscountPrice(order.getAchPrice());
                                payment.setTransferPrice(order.getAchPrice());
                            }
                        } else {
                            payment.setDiscountPrice(order.getAchPrice());
                            payment.setTransferPrice(order.getAchPrice());
                        }
                    } else {
                        payment.setDiscountPrice(new BigDecimal(totalAmount));
                        payment.setTransferPrice(order.getAchPrice().subtract(order.getCommissionsPrice()));
                    }
                    paymentService.transfer(payment);
                    Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
                    if ((Order.TitleType.BIGTHING + "").equals(order.getTitle() + "")) {
                        PushUtils.getAcsResponse(recyclers.getTel(), "3", order.getTitle().getValue() + "");
                    }
                    try {
                        if (order.getAddress().startsWith("上海市") && (Order.TitleType.HOUSEHOLD + "").equals(order.getTitle() + "")) {
                            NewThreadPoorExcutor.getThreadPoor().execute(new Thread(new sendGreenOrderThread(orderService, areaService, orderItemAchService, order.getId().intValue())));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (Order.TitleType.DIGITAL.equals(order.getTitle()) && new BigDecimal("0.1").compareTo(new BigDecimal(totalAmount)) == 0) {
                        Jedis resource = jedisPool.getResource();
                        try {
                            if (StringUtils.isNotBlank(resource.hget("lowPriceRecycler" + recyclers.getTel(), "limitNum"))) {
                                String toDayCompleteCount = resource.hget("lowPriceRecycler" + recyclers.getTel(), DateUtils.getDate() + "_completeCount");
                                if (StringUtils.isNotBlank(toDayCompleteCount)) {
                                    resource.hset("lowPriceRecycler" + recyclers.getTel(), DateUtils.getDate() + "_completeCount", "1");
                                } else {
                                    int count = Integer.parseInt(toDayCompleteCount) + 1;
                                    resource.hset("lowPriceRecycler" + recyclers.getTel(), DateUtils.getDate() + "_completeCount", String.valueOf(count));
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            resource.close();
                        }

                    }

                } else if (tradeStatus.equalsIgnoreCase("TRADE_CLOSED")) {
                    if (null != voucherMember) {
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


    /**
     * 微信异步通知
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/wx")
    public @ResponseBody
    String payNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);

            String outTradeNo = result.getOutTradeNo();
            String tradeNo = result.getTransactionId();
            logger.info("收到微信异步通知------------------参数：" + JSON.toJSONString(result));
            String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());

            //判断订单类型
            PrepayOrder jlPrepayOrder = prepayOrderService.findByOutTradeNo(outTradeNo);
            //先查找支付成功的记录有没有
            WxPayment wxPayment = wxPaymentService.findByOutTradeNo(outTradeNo);
            if (wxPayment != null) {
                //说明已经处理过了直接return
                return WxPayNotifyResponse.success("处理成功!");
            }
            //根據order_no查询相关订单
            Order order = orderService.getByOrderNo(jlPrepayOrder.getOrderCode());

            if (order != null) {
                //如果订单完成了就直接返回成功
                if (Order.OrderType.COMPLETE.equals(order.getStatus())) {
                    return WxPayNotifyResponse.success("处理成功!");
                }
                //根据订单号查询绑定券的信息
                VoucherMember voucherMember = voucherMemberService.selectOne(new EntityWrapper<VoucherMember>().eq("order_no", order.getOrderNo()).eq("ali_user_id", order.getAliUserId()));
                wxPayment = new WxPayment();
                BeanUtils.copyProperties(jlPrepayOrder, wxPayment);
                wxPayment.setTransactionId(tradeNo);
                wxPayment.setPayStatus(WxPayment.SUCCESS);
                wxPayment.setTotalFee(new BigDecimal(totalFee));
                wxPayment.setOrderNo(jlPrepayOrder.getOrderCode());
                wxPayment.setTotalAmount(totalFee);
                wxPayment.setVoucherMember(voucherMember);
                wxPaymentService.insert(wxPayment);
                //給用戶轉賬
                if (!(Order.TitleType.BIGTHING + "").equals(order.getTitle() + "")) {
                    if (null != voucherMember) {
                        //如果不是大件并且有优惠券，则计算使用该券后的价格给用户进行转账
                        BigDecimal discountPrice = voucherAliService.getDiscountPriceByVoucherId(order.getAchPrice(), voucherMember.getId().toString());
                        wxPayment.setDiscountPrice(discountPrice);
                        wxPayment.setTransferPrice(discountPrice);
                    } else {
                        wxPayment.setDiscountPrice(order.getAchPrice());
                        wxPayment.setTransferPrice(order.getAchPrice());
                    }
                } else {
                    wxPayment.setDiscountPrice(new BigDecimal(totalFee));
                    wxPayment.setTransferPrice(order.getAchPrice().subtract(order.getCommissionsPrice()));
                }
                wxPaymentService.transfer(wxPayment);
                Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
                if ((Order.TitleType.BIGTHING + "").equals(order.getTitle() + "")) {
                    PushUtils.getAcsResponse(recyclers.getTel(), "3", order.getTitle().getValue() + "");
                }
                try {
                    if (order.getAddress().startsWith("上海市") && (Order.TitleType.HOUSEHOLD + "").equals(order.getTitle() + "")) {
                        NewThreadPoorExcutor.getThreadPoor().execute(new Thread(new sendGreenOrderThread(orderService, areaService, orderItemAchService, order.getId().intValue())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }
}
