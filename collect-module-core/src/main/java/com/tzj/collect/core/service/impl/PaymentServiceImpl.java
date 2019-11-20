package com.tzj.collect.core.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.constant.ApplicaInit;
import com.tzj.collect.core.mapper.PaymentMapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.entity.PaymentError;
import com.tzj.collect.entity.Recyclers;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.UUID;

import static com.tzj.collect.common.constant.Const.*;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RecyclersService recyclersService;
    @Autowired
    private ApplicaInit applicaInit;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private PaymentErrorService paymentErrorService;

    @Override
    public Payment selectByOrderSn(String orderNo) {
        return selectOne(new EntityWrapper<Payment>().eq("order_sn", orderNo));
    }
    @Override
    public Payment selectByOutTradeNo(String outTradeNo){
        return selectOne(new EntityWrapper<Payment>().eq("out_trade_no", outTradeNo));
    }

    @Override
    public Payment selectPayByOrderSn(String orderNo){
        return selectOne(new EntityWrapper<Payment>().eq("order_sn", orderNo).eq("status_",Payment.STATUS_PAYED));
    }

    /**
     * 小程序支付
     */
    @Override
    public String genalPayXcx(Payment payment) {
        Assert.notNull(payment, "payment不能为空！");
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        String sn = payment.getOrderSn();
        String subject = "垃圾分类回收订单(收呗):" + sn;

        model.setBody(subject);
        model.setSubject(subject);
        model.setOutTradeNo(payment.getOutTradeNo());
        model.setTimeoutExpress("2m");
        model.setBuyerId(payment.getAliUserId());
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088421446748174");
        model.setExtendParams(extendParams);
        model.setTotalAmount(payment.getPrice().setScale( 2, BigDecimal.ROUND_HALF_UP).toString());
        request.setBizModel(model);
        request.setNotifyUrl(applicaInit.getNotifyUrl());
        try {
            //这里和普通的接口调用不同，使用的是sdkExecutee
            AlipayTradeCreateResponse response = alipayClient.execute(request);
            return response.getTradeNo();
        } catch (AlipayApiException e) {
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
    }
    /**
     * 支付宝支付
     *
     * @param payment
     * @return
     */
    @Override
    public String genalPay(Payment payment) {

        Assert.notNull(payment, "payment不能为空！");

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        String sn = payment.getOrderSn();
        String subject = "垃圾分类回收订单(收呗):" + sn;

        model.setBody(subject);
        model.setSubject(subject);
        model.setOutTradeNo(payment.getOutTradeNo());
        model.setTimeoutExpress("2m");
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2017011905224137");
        model.setExtendParams(extendParams);
        model.setTotalAmount(payment.getPrice().setScale( 2, BigDecimal.ROUND_HALF_UP).toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(applicaInit.getNotifyUrl());
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return response.getBody();
        } catch (AlipayApiException e) {
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
    }

    /**
     * 支付宝转账
     *
     * @param payment
     */
    @Override
    @Transactional
    public void transfer(Payment payment) {

        Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", payment.getOrderSn()));
        PaymentError paymentError = paymentErrorService.selectOne(new EntityWrapper<PaymentError>().eq("order_sn",payment.getOrderSn()));
        if (null==paymentError){
            paymentError = new PaymentError();
        }
        OrderBean orderBean = new OrderBean();
        if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
            orderBean.setAchPrice(payment.getPrice().toString());
            orderBean.setDiscountPrice(payment.getTotalAmount());
        }else {
            orderBean.setAchPrice(payment.getTotalAmount());
            orderBean.setDiscountPrice(payment.getPrice().toString());
        }
        String aliUserId = "";
        if ((order.getTitle()+"").equals(Order.TitleType.BIGTHING+"")){
            Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
            aliUserId = recyclers.getAliUserId();
        }else {
            aliUserId = payment.getAliUserId();
        }
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response =this.aliPayTransfer(payment.getId().toString(),aliUserId,payment.getPrice());
            if (response.isSuccess()||"10000".equals(response.getCode())) {
                payment.setStatus(Payment.STATUS_TRANSFER);
                //修改订单状态
                orderBean.setId(order.getId().intValue());
                orderBean.setStatus("3");
                orderBean.setAmount(order.getGreenCount());
                orderService.modifyOrderByPayment(orderBean,payment.getVoucherMember());
            }else {
                paymentError.setReason(response.getBody());
                paymentError.setTitle(response.getSubMsg());
                paymentError.setNotifyType(response.getCode());
                paymentError.setSendUser(order.getTel());
                paymentError.setReceiveUser(order.getRecyclerId().toString());
                paymentError.setOrderSn(order.getOrderNo());
                paymentErrorService.insertOrUpdate(paymentError);
                asyncService.notifyDingDingPaymentError(order.getOrderNo(),response.getBody(), PaymentError.DING_DING_URL,PaymentError.DING_DING_SING,PaymentError.DING_DING_TEL);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (!(response.isSuccess()||"10000".equals(response.getCode()))) {
                order.setAchPrice(new BigDecimal(orderBean.getAchPrice()));
                order.setDiscountPrice(new BigDecimal(orderBean.getDiscountPrice()));
                orderService.updateById(order);
            }
            payment.setRemarks(response.getSubMsg());
            this.updateById(payment);
        }
    }

    //支付宝转账
    public AlipayFundTransToaccountTransferResponse aliPayTransfer(String outBizNo,String aliUserId,BigDecimal amount) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
            model.setOutBizNo(outBizNo);
            model.setPayeeType("ALIPAY_USERID"); //ALIPAY_LOGONID  ALIPAY_USERID
            model.setPayeeAccount(aliUserId);
            model.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            model.setPayerShowName("垃圾分类回收(收呗)货款");
            model.setRemark("垃圾分类回收(收呗)货款");
        request.setBizModel(model);
        return alipayClient.execute(request);
    }

    public static void main(String[] args) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(UUID.randomUUID().toString());
        model.setPayeeType("ALIPAY_USERID"); //ALIPAY_LOGONID  ALIPAY_USERID
        model.setPayeeAccount("2088212854989662");
        model.setAmount("0.11");
        model.setPayerShowName("垃圾分类回收(收呗)货款");
        model.setRemark("垃圾分类回收(收呗)货款");
        request.setBizModel(model);
        AlipayFundTransToaccountTransferResponse r = alipayClient.execute(request);
        System.out.println(r.isSuccess());
        System.out.println(r.getSubMsg());
        System.out.println(r.getSubCode());
        System.out.println(r.getCode());
    }
    /**
     * 根据支付宝交易号查询该交易的详细信息
     * @param tradeNo
     */
    public AlipayTradeQueryResponse getAliPayment(String tradeNo){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(tradeNo);
            model.setTradeNo(tradeNo);
            request.setBizModel(model);

        AlipayTradeQueryResponse response = null;
        try{
            response = alipayClient.execute(request);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用查询交易接口成功 : "+response.getBody());
        } else {
            System.out.println("调用查询交易接口失败 : "+response.getBody());
        }
        return response;
    }
    
    /**
     * 查询转账信息
     */
    @Override
    public AlipayFundTransOrderQueryResponse getTransfer(String paymentId) {
    	AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
    	AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
    	request.setBizContent("{" +
    	"\"out_biz_no\":\""+paymentId+"\"" +
    	//"\"order_id\":\"20160627110070001502260006780837\"" +
    	"  }");
    	AlipayFundTransOrderQueryResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(response.isSuccess()){
    	System.out.println("调用成功");
    	} else {
    	System.out.println("调用失败");
    	}
       return response;
    }

    public  AlipayFundTransToaccountTransferResponse receivingMoneyTransfer(String aliUserId, String price, String outBizNo){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(outBizNo);
        model.setPayeeType("ALIPAY_USERID");
        model.setPayeeAccount(aliUserId);
        model.setAmount(price);
        model.setPayerShowName("码上回收现金红包活动");
        model.setRemark("码上回收现金红包活动");
        request.setBizModel(model);
        AlipayFundTransToaccountTransferResponse response =null;
        try {
            response  = alipayClient.execute(request);
//            System.out.println(response);
        }catch (AlipayApiException e){
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
        return response;

    }

    /**
     * 交易关闭
     * @param outTradeNo
     * @return
     */
    @Override
    public AlipayTradeCloseResponse paymentCloseByTradeNo(String outTradeNo){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
            //model.setTradeNo(tradeNo);
            model.setOutTradeNo(outTradeNo);
            request.setBizModel(model);
        AlipayTradeCloseResponse response = null;
        try{
            response = alipayClient.execute(request);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        System.out.println(response.getBody());
        return response;
    }


    public  AlipayFundTransToaccountTransferResponse iotTransfer(String aliUserId, String price, String outBizNo){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(outBizNo);
        model.setPayeeType("ALIPAY_USERID");
        model.setPayeeAccount(aliUserId);
        model.setAmount(price);
        model.setPayerShowName("支付宝垃圾分类回收");
        model.setRemark("定金回退");
        request.setBizModel(model);
        AlipayFundTransToaccountTransferResponse response =null;
        try {
            response  = alipayClient.execute(request);
//            System.out.println(response);
        }catch (AlipayApiException e){
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
        return response;
    }
}
