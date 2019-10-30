package com.tzj.collect.core.service.impl;

import com.alibaba.druid.pool.ha.selector.StickyRandomDataSourceSelector;
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
        model.setOutTradeNo(sn);
        model.setTimeoutExpress("15d");
        model.setBuyerId(payment.getAliUserId());
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088421446748174");
        model.setExtendParams(extendParams);
        model.setTotalAmount(payment.getPrice().setScale( 2, BigDecimal.ROUND_HALF_UP).toString());
        request.setBizModel(model);
        if ("true".equals(applicaInit.getIsTestPayment())){
            request.setNotifyUrl("http://shoubeics.mayishoubei.com/notify/alipay");
        }else {
            request.setNotifyUrl("http://open.mayishoubei.com/notify/alipay");
        }
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
        model.setOutTradeNo(sn);
        model.setTimeoutExpress("15d");
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2017011905224137");
        model.setExtendParams(extendParams);
        model.setTotalAmount(payment.getPrice().setScale( 2, BigDecimal.ROUND_HALF_UP).toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        if ("true".equals(applicaInit.getIsTestPayment())){
            request.setNotifyUrl("http://shoubeics.mayishoubei.com/notify/alipay");
        }else {
            request.setNotifyUrl("http://open.mayishoubei.com/notify/alipay");
        }
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return response.getBody();
        } catch (AlipayApiException e) {
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
    }

    public static void main(String[] args) throws Exception{
//        String uuId = UUID.randomUUID().toString();
//        //dailyDaTransfer("2088212384105273","0.01", uuId);
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
//        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
//        request.setBizContent("{" +
//                "\"out_biz_no\":\""+732618+"\"" +
//                //"\"order_id\":\"20160627110070001502260006780837\"" +
//                "  }");
//        AlipayFundTransOrderQueryResponse response = null;
//        try {
//            response = alipayClient.execute(request);
//        } catch (AlipayApiException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        if(response.isSuccess()){
//            System.out.println("调用成功");
//        } else {
//            System.out.println("调用失败");
//        }
//        System.out.println(response.getBody());

//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
//        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
//        String tradeNo = uuId;
//        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
//        model.setOutTradeNo(tradeNo);
//        model.setTradeNo(tradeNo);
//        request.setBizModel(model);
//
//        AlipayTradeQueryResponse response = null;
//        try{
//            response = alipayClient.execute(request);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        if(response.isSuccess()){
//            System.out.println("调用查询交易接口成功 : "+response.getBody());
//        } else {
//            System.out.println("调用查询交易接口失败 : "+response.getBody());
//        }
//        System.out.println(response.getBody());

//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
//        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//
//        String sn = "20190624174741561944";
//        String subject = "垃圾分类回收订单(收呗):" + sn;
//
//        model.setBody(subject);
//        model.setSubject(subject);
//        model.setOutTradeNo(sn);
//        model.setTimeoutExpress("15d");
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2017011905224137");
//        model.setExtendParams(extendParams);
//        model.setTotalAmount("10");
//        model.setProductCode("QUICK_MSECURITY_PAY");
//        request.setBizModel(model);
//        request.setNotifyUrl("http://open.mayishoubei.com/notify/alipay");
//        try {
//            //这里和普通的接口调用不同，使用的是sdkExecute
//            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//            System.out.println(response.getBody());
//        } catch (AlipayApiException e) {
//            throw new ApiException("系统异常：" + e.getErrMsg());
//        }
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
            if (response.isSuccess()) {
                payment.setStatus(Payment.STATUS_TRANSFER);
                //修改订单状态
                orderBean.setId(order.getId().intValue());
                orderBean.setStatus("3");
                orderBean.setAmount(order.getGreenCount());
                orderService.modifyOrderByPayment(orderBean,payment.getVoucherMember());
            }else {
                order.setAchPrice(new BigDecimal(orderBean.getAchPrice()));
                order.setDiscountPrice(new BigDecimal(orderBean.getDiscountPrice()));
                orderService.updateById(order);
                PaymentError paymentError = new PaymentError();
                paymentError.setReason(response.getBody());
                paymentError.setTitle(response.getSubMsg());
                paymentError.setNotifyType(response.getCode());
                paymentError.setSendUser(order.getTel());
                paymentError.setReceiveUser(order.getRecyclerId().toString());
                paymentError.setOrderSn(order.getOrderNo());
                paymentErrorService.insert(paymentError);
                asyncService.notifyDingDingPaymentError(order.getOrderNo(),response.getBody(), PaymentError.DING_DING_URL,PaymentError.DING_DING_SING,PaymentError.DING_DING_TEL);
            }
        }catch (AlipayApiException e){
            order.setAchPrice(new BigDecimal(orderBean.getAchPrice()));
            order.setDiscountPrice(new BigDecimal(orderBean.getDiscountPrice()));
            orderService.updateById(order);
            PaymentError paymentError = new PaymentError();
            paymentError.setReason(response.getBody());
            paymentError.setTitle(response.getSubMsg());
            paymentError.setNotifyType(response.getCode());
            paymentError.setSendUser(order.getTel());
            paymentError.setReceiveUser(order.getRecyclerId().toString());
            paymentError.setOrderSn(order.getOrderNo());
            paymentErrorService.insert(paymentError);
            asyncService.notifyDingDingPaymentError(order.getOrderNo(),response.getBody()+"系统异常 : "+ e.getErrMsg(), PaymentError.DING_DING_URL,PaymentError.DING_DING_SING,PaymentError.DING_DING_TEL);
        }finally {
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
}
