package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.binarywang.wxpay.bean.entpay.EntPayQueryRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayQueryResult;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.tzj.collect.api.commom.constant.WXConst;
import com.tzj.collect.core.mapper.PrepayOrderMapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.core.service.RefundService;
import com.tzj.collect.core.service.WXPayService;
import com.tzj.collect.core.service.WxTransferService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.PrepayOrder;
import com.tzj.collect.entity.Refund;
import com.tzj.collect.entity.WxTransfer;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020-03-18 17:44
 * @Description:
 */
@Service
@Transactional(readOnly = true)
@EnableAutoConfiguration
public class WXPayServiceImpl implements WXPayService {

    protected final static Logger logger = LoggerFactory.getLogger(WXPayServiceImpl.class);
    @Resource(name = "wxPayService")
    WxPayService wxPayService;
    @Resource(name = "wxAppPayService")
    WxPayService wxAppPayService;
    @Autowired
    PrepayOrderMapper PrepayOrderMapper;
    @Resource(name = "entpayService")
    EntPayService entPayService;
    @Resource(name = "appEntpayService")
    EntPayService appEntPayService;
    @Autowired
    WxTransferService transferService;
    @Autowired
    RefundService RefundService;
    @Autowired
    OrderService orderService;
    @Value("${wxNotifyUrl}")
    private String  wxNotifyUrl;


    @Transactional(noRollbackFor = ApiException.class)
    @Override
    public Object prePay(String openId, String orderNo, String from, BigDecimal totalFee, String payBody, String tradeType) {
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        PrepayOrder jlPrepayOrder = new PrepayOrder();
        try {
            String outTradeNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(89999999) + 10000000);
            orderRequest.setBody(payBody);
            orderRequest.setOutTradeNo(outTradeNo);
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(totalFee.toString()));
            orderRequest.setOpenid(openId);
            orderRequest.setSpbillCreateIp("127.0.0.1");
            orderRequest.setTimeStart(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis() + 900000L);
            orderRequest.setTimeExpire(DateUtils.formatDate(calendar.getTime(), "yyyyMMddHHmmss"));
            orderRequest.setNotifyUrl(wxNotifyUrl);
            orderRequest.setTradeType(from);
            jlPrepayOrder.setFrom(from);
            jlPrepayOrder.setOpenId(openId);
            jlPrepayOrder.setOutTradeNo(outTradeNo);
            jlPrepayOrder.setOrderCode(orderNo);
            jlPrepayOrder.setRequestParams(JSON.toJSONString(orderRequest));
            jlPrepayOrder.setTradeType(tradeType);
            jlPrepayOrder.setTotalfee(totalFee);
            Object result;
            if (WXConst.FROM_APP.equals(from)) {
                result = wxAppPayService.createOrder(orderRequest);
            } else {
                result = wxPayService.createOrder(orderRequest);
            }
            jlPrepayOrder.setResponseParams(JSON.toJSONString(result));
            jlPrepayOrder.setStatus(PrepayOrder.INIT_SUCCESS);
            return result;
        } catch (Exception e) {
            logger.error("微信支付失败！参数：{},原因:{}", JSON.toJSON(orderRequest), e.getMessage());
            e.printStackTrace();
            jlPrepayOrder.setResponseParams(e.getMessage());
            jlPrepayOrder.setStatus(PrepayOrder.INIT_FAIL);
            throw new ApiException("支付失败，" + e.getMessage());
        } finally {
            PrepayOrderMapper.insert(jlPrepayOrder);
        }
    }


    /**
     * 微信支付查询订单
     *
     * @param orderPayParam
     * @return
     * @throws WxPayException
     */
    @Override
    public boolean queryOrder(String transactionId, String outTradeNo) {
        try {
            WxPayOrderQueryResult wxPayOrderQueryResult = this.wxPayService.queryOrder(transactionId, outTradeNo);
            if (wxPayOrderQueryResult.getResultCode().equals(WXConst.SUCCESS_CODE) &&
                    wxPayOrderQueryResult.getTradeState().equals(WXConst.SUCCESS_CODE) &&
                    wxPayOrderQueryResult.getReturnCode().equals(WXConst.SUCCESS_CODE)) {
                return true;
            }

        } catch (Exception e) {
            logger.error("微信支付查询订单！参数：{},原因:{}", transactionId + "   " + outTradeNo, e.getMessage());
            e.printStackTrace();

        }
        return false;
    }


    /**
     * 转账
     *
     * @param partnerTardeNo
     * @param openId
     * @param amount
     * @param desc
     */
    @Transactional(noRollbackFor = ApiException.class)
    @Override
    public void transfer(String partnerTardeNo, String openId, BigDecimal amount, String desc, String from) {
        EntPayRequest entPayRequest = new EntPayRequest();
        WxTransfer jlTransfer = new WxTransfer();
        try {
            entPayRequest.setAmount(BaseWxPayRequest.yuanToFen(amount.toString()));
            entPayRequest.setCheckName("NO_CHECK");
            entPayRequest.setDescription(desc);
            entPayRequest.setOpenid(openId);
            entPayRequest.setPartnerTradeNo(partnerTardeNo);
            entPayRequest.setSpbillCreateIp("127.0.0.1");
            jlTransfer.setAmount(amount);
            jlTransfer.setPartnerTradeNo(partnerTardeNo);
            jlTransfer.setOpenId(openId);
            jlTransfer.setRemarks(desc);
            jlTransfer.setRequestParams(JSON.toJSONString(entPayRequest));
            jlTransfer.setFrom(from);
            EntPayResult entPayResult;
            if (WXConst.FROM_APP.equals(from)) {
                entPayResult = appEntPayService.entPay(entPayRequest);
            } else {
                entPayResult = entPayService.entPay(entPayRequest);
            }
            if (entPayResult.getReturnCode().equals(WXConst.SUCCESS_CODE)
                    && entPayResult.getResultCode().equals(WXConst.SUCCESS_CODE)) {
                jlTransfer.setPaymentNo(entPayResult.getPaymentNo());
                jlTransfer.setMchAppid(entPayResult.getMchAppid());
                jlTransfer.setMchid(entPayResult.getMchId());
                jlTransfer.setResultCode(WXConst.SUCCESS_CODE);
                jlTransfer.setResponseParams(JSONObject.toJSONString(entPayResult));
            }
        } catch (Exception e) {
            logger.error("微信转账接口失败！参数：{},原因:{}", JSON.toJSONString(entPayRequest), e.getMessage());
            e.printStackTrace();
            jlTransfer.setResponseParams(e.getMessage());
            jlTransfer.setResultCode(WXConst.FAIL_CODE);
            throw new ApiException("微信转账接口失败！ 原因:"+ e.getMessage());
        } finally {
            transferService.insert(jlTransfer);
        }
    }

    @Transactional
    @Override
    public void transferRetry(WxTransfer jlTransfer) {
        EntPayRequest entPayRequest = new EntPayRequest();
        try {
            entPayRequest.setAmount(BaseWxPayRequest.yuanToFen(jlTransfer.getAmount().toString()));
            entPayRequest.setCheckName("NO_CHECK");
            entPayRequest.setDescription(jlTransfer.getRemarks());
            entPayRequest.setOpenid(jlTransfer.getOpenId());
            entPayRequest.setPartnerTradeNo(jlTransfer.getPartnerTradeNo());
            entPayRequest.setSpbillCreateIp("127.0.0.1");
            EntPayResult entPayResult;
            if (WXConst.FROM_APP.equals(jlTransfer.getFrom())) {
                entPayResult = appEntPayService.entPay(entPayRequest);
            } else {
                entPayResult = entPayService.entPay(entPayRequest);
            }
            if (entPayResult.getReturnCode().equals(WXConst.SUCCESS_CODE)
                    && entPayResult.getResultCode().equals(WXConst.SUCCESS_CODE)) {
                jlTransfer.setPaymentNo(entPayResult.getPaymentNo());
                jlTransfer.setMchAppid(entPayResult.getMchAppid());
                jlTransfer.setMchid(entPayResult.getMchId());
                jlTransfer.setResultCode(WXConst.SUCCESS_CODE);
                jlTransfer.setResponseParams(JSONObject.toJSONString(entPayResult));
                //转账完成后，完成订单
                //TODO  后续接口参数传入mqtt
                this.orderComplete(jlTransfer.getPartnerTradeNo(), jlTransfer.getAmount().toString(),null);
            }
        } catch (Exception e) {
            logger.error("微信转账重试接口失败！参数：{},原因:{}", JSON.toJSONString(entPayRequest), e.getMessage());
            // e.printStackTrace();
            jlTransfer.setResponseParams(e.getMessage());
            jlTransfer.setResultCode(WXConst.FAIL_CODE);
        } finally {
            transferService.update(jlTransfer, new EntityWrapper<WxTransfer>().eq("id", jlTransfer.getId()));
        }
    }

    @Override
    public EntPayQueryResult transferQuery(String partnerTradeNo, String from) {
        EntPayQueryRequest entPayQueryRequest = new EntPayQueryRequest();
        try {
            entPayQueryRequest.setPartnerTradeNo(partnerTradeNo);
            EntPayQueryResult entPayQueryResult;
            if (WXConst.FROM_APP.equals(from)) {
                entPayQueryResult = appEntPayService.queryEntPay(entPayQueryRequest);
            } else {
                entPayQueryResult = entPayService.queryEntPay(entPayQueryRequest);
            }

            return entPayQueryResult;
        } catch (Exception e) {
            logger.error("微信查询接口失败！参数：{},原因:{}", partnerTradeNo, e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * 退款
     *
     * @param outTradeNo 商户订单号
     * @param totalFee   订单总金额
     * @param refundFee  退款金额
     * @return
     */
    @Transactional
    @Override
    public boolean refund(String outTradeNo, BigDecimal totalFee, BigDecimal refundFee) {
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        WxPayRefundResult wxPayRefundResult = new WxPayRefundResult();
        Refund jlRefund = new Refund();
        try {
            wxPayRefundRequest.setRefundFee(BaseWxPayRequest.yuanToFen(refundFee.toString()));
            wxPayRefundRequest.setTotalFee(BaseWxPayRequest.yuanToFen(totalFee.toString()));
            wxPayRefundRequest.setOutTradeNo(outTradeNo);
            wxPayRefundRequest.setOutRefundNo(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(89999999) + 10000000));
            jlRefund.setOutRefundNo(wxPayRefundRequest.getOutRefundNo());
            jlRefund.setOutTradeNo(wxPayRefundRequest.getOutTradeNo());
            jlRefund.setRefundFee(refundFee);
            jlRefund.setTotalFee(totalFee);
            jlRefund.setRequestParams(JSON.toJSONString(wxPayRefundRequest));
            wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
            if (wxPayRefundResult.getReturnCode().equals(WXConst.SUCCESS_CODE)
                    && wxPayRefundResult.getResultCode().equals(WXConst.SUCCESS_CODE)) {
                jlRefund.setStatus(WXConst.SUCCESS_CODE);
                return true;
            }
        } catch (WxPayException e) {
            logger.error("微信退款接口失败！参数：{},原因:{}", JSON.toJSONString(wxPayRefundRequest), e.getMessage());
            e.printStackTrace();
            wxPayRefundResult.setResultCode(WXConst.FAIL_CODE);
            jlRefund.setStatus(WXConst.FAIL_CODE);
            return false;
        } finally {
            RefundService.insert(jlRefund);
        }
        return false;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderComplete(String orderCode, String totalFee,MqttClient mqttClient) {
        Order order = orderService.getByOrderNo(orderCode);
        if (order != null) {
            //判断订单是否已完成
            if (!order.getStatus().equals(3)) {
                //变更订单状态
                OrderBean orderBean = new OrderBean();
                orderBean.setId(Integer.parseInt(order.getId() + ""));
                orderBean.setAmount(order.getGreenCount());
                orderBean.setAchPrice(totalFee);
                orderBean.setStatus("3");
                orderService.modifyOrderSta(orderBean,mqttClient);
            }
        }
    }


}
