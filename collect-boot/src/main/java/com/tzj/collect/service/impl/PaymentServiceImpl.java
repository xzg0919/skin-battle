package com.tzj.collect.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.constant.RocketMqConst;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.mapper.PaymentMapper;
import com.tzj.collect.service.OrderService;
import com.tzj.collect.service.PaymentService;
import com.tzj.collect.service.RecyclersService;
import com.tzj.module.common.notify.dingtalk.DingTalkNotify;
import com.tzj.module.easyopen.exception.ApiException;
import net.sf.ehcache.search.expression.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.tzj.collect.api.common.constant.Const.*;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RecyclersService recyclersService;

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
        request.setNotifyUrl("http://test.mayishoubei.com/notify/alipay.jhtml");
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
        request.setNotifyUrl("http://open.mayishoubei.com/notify/alipay.jhtml");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return response.getBody();
        } catch (AlipayApiException e) {
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
    }

    public static void main(String[] args) {
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APP_APPID, ALI_APP_PAY_KEY, "json", "UTF-8", ALI_APP_PUBLIC_KEY, "RSA2");
//        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
//        request.setBizContent("{" +
//                "\"out_biz_no\":\""+5244+"\"" +
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
//        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
//
//
//        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
//        model.setOutBizNo("2018040209312523257");
//        model.setPayeeType("ALIPAY_USERID");
//        model.setPayeeAccount("2088212854989662");
//        model.setAmount("0.1");
//        model.setPayerShowName("垃圾分类回收(收呗)货款");
//        model.setRemark("垃圾分类回收(收呗)货款");
//
//        request.setBizModel(model);
//        try {
//            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
//            if (response.isSuccess()){
//                response.getBody();
//            }else {
//                System.out.println("转账异常");
//            }
//        }catch (AlipayApiException e){
//            throw new ApiException("系统异常：" + e.getErrMsg());
//        }
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        String sn = orderNo;
        String subject = "垃圾分类回收订单(收呗):" + sn;

        model.setOutTradeNo(sn);
        model.setTotalAmount("1");
        model.setSubject(subject);
        model.setTimeoutExpress("15d");
        model.setBuyerId("2088212854989662");
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088421446748174");
        model.setExtendParams(extendParams);
        request.setBizModel(model);
        request.setNotifyUrl("http://open.mayishoubei.com/notify/alipay.jhtml");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecutee
            AlipayTradeCreateResponse response = alipayClient.execute(request);
           if (response.isSuccess()){
               System.out.println(response.getTradeNo());
           }else {
               System.out.println(response.getBody());
               DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                       ,Thread.currentThread().getStackTrace()[1].getMethodName(),"转账失败",
                       RocketMqConst.DINGDING_ERROR,response.getBody());
           }
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


        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();


        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(payment.getId().toString());
        //ALIPAY_LOGONID  ALIPAY_USERID
        model.setPayeeType("ALIPAY_USERID");
        if ((order.getTitle()+"").equals(Order.TitleType.BIGTHING+"")){
            Recyclers recyclers = recyclersService.selectById(order.getId());
            model.setPayeeAccount(recyclers.getAliUserId());
        }else {
            model.setPayeeAccount(payment.getAliUserId());
        }
        model.setAmount(payment.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        model.setPayerShowName("垃圾分类回收(收呗)货款");
        model.setRemark("垃圾分类回收(收呗)货款");

        request.setBizModel(model);
        try {
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                payment.setStatus(Payment.STATUS_TRANSFER);
                insertOrUpdate(payment);
            }
        }catch (AlipayApiException e){
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
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
    	AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APP_APPID, ALI_APP_PAY_KEY, "json", "UTF-8", ALI_APP_PUBLIC_KEY, "RSA2");
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

}
