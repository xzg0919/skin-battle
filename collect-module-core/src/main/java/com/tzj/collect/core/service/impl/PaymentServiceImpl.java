package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.constant.ApplicaInit;
import com.tzj.collect.core.mapper.PaymentMapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Member;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private MemberService memberService;
    @Resource
    private MemberXianyuService memberXianyuService;
    @Autowired
    private RedisUtil redisUtil;

    @Resource(name = "certAlipayClient")
    AlipayClient certAlipayClient;

    @Override
    public Payment selectByOrderSn(String orderNo) {
        return selectOne(new EntityWrapper<Payment>().eq("order_sn", orderNo).in("status_", "1,2"));
    }

    @Override
    public Payment selectByOutTradeNo(String outTradeNo) {
        return selectOne(new EntityWrapper<Payment>().eq("out_trade_no", outTradeNo));
    }

    @Override
    public Payment selectPayByOrderSn(String orderNo) {
        Payment payment = selectOne(new EntityWrapper<Payment>().eq("order_sn", orderNo).eq("status_", Payment.STATUS_PAYED));
        if (null == payment) {
            payment = selectOne(new EntityWrapper<Payment>().eq("order_sn", orderNo).eq("status_", Payment.STATUS_TRANSFER));
        }
        return payment;
    }

    /**
     * 小程序支付
     */
    @Override
    @Transactional
    public String genalPayXcx(Payment payment, Order order) {
        Assert.notNull(payment, "payment不能为空！");
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
        model.setTotalAmount(payment.getPrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
        request.setBizModel(model);
        request.setNotifyUrl(applicaInit.getNotifyUrl());
        try {
            //这里和普通的接口调用不同，使用的是sdkExecutee
            AlipayTradeCreateResponse response = certAlipayClient.certificateExecute(request);
            payment.setTradeNo(response.getTradeNo());
            this.updateById(payment);
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
    public String genalPay(Payment payment, Order order) {
        Assert.notNull(payment, "payment不能为空！");

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
        model.setTotalAmount(payment.getPrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(applicaInit.getNotifyUrl());
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = certAlipayClient.sdkExecute(request);
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
        PaymentError paymentError = paymentErrorService.selectOne(new EntityWrapper<PaymentError>().eq("order_sn", payment.getOrderSn()));
        if (null == paymentError) {
            paymentError = new PaymentError();
        }
        //乐观锁原因需重新查询一次得到真实payment
        // todo
        Payment payment1 = this.selectById(payment.getId());
        OrderBean orderBean = new OrderBean();
        orderBean.setDiscountPrice(payment.getDiscountPrice().toString());
        String aliUserId = "";
        String payeeType = "ALIPAY_USER_ID";
        if ((order.getTitle() + "").equals(Order.TitleType.BIGTHING + "")) {
            Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
            aliUserId = recyclers.getAliUserId();
        } else {
            aliUserId = payment.getAliUserId();
            if ("2".equals(order.getOrderFrom())) {
                aliUserId = order.getAliAccount();
                payeeType = "ALIPAY_LOGON_ID";
            }
        }
        AlipayFundTransUniTransferResponse response = null;

        if (payment.getTransferPrice().compareTo(BigDecimal.ZERO) == 0) {
            payment1.setStatus(Payment.STATUS_TRANSFER);
            if (("1".equals(order.getIsMysl()) && (order.getStatus() + "").equals(Order.OrderType.ALREADY + "")) || order.getIsScan().equals("1")) {
                //给用户增加蚂蚁能量
                orderService.myslOrderData(order.getId().toString());
            }
            //修改订单状态
            orderBean.setId(order.getId().intValue());
            orderBean.setStatus("3");
            orderBean.setAmount(order.getGreenCount());
            orderService.modifyOrderByPayment(orderBean, payment.getVoucherMember());
            if (Order.OrderType.CANCEL.equals(order.getStatus())) {
                payment1.setDelFlag("1");
            }
            this.updateById(payment1);
        } else {
            try {
                response = this.aliPayTransfer(payment.getOrderSn(), aliUserId, payment.getTransferPrice(), payeeType);
                if ((response.isSuccess() && "10000".equals(response.getCode()))) {
                    payment1.setStatus(Payment.STATUS_TRANSFER);
                    if (("1".equals(order.getIsMysl()) && (order.getStatus() + "").equals(Order.OrderType.ALREADY + "")) || order.getIsScan().equals("1")) {
                        //给用户增加蚂蚁能量
                        orderService.myslOrderData(order.getId().toString());
                    }
                    //修改订单状态
                    orderBean.setId(order.getId().intValue());
                    orderBean.setStatus("3");
                    orderBean.setAmount(order.getGreenCount());
                    orderService.modifyOrderByPayment(orderBean, payment.getVoucherMember());
                } else {
                    System.out.println("转账失败，保存错误信息");
                    paymentError.setReason(response.getBody());
                    paymentError.setTitle(response.getSubMsg());
                    paymentError.setNotifyType(response.getCode());
                    paymentError.setSendUser(order.getTel());
                    paymentError.setReceiveUser(order.getRecyclerId().toString());
                    paymentError.setOrderSn(order.getOrderNo());
                    //如果是账号有问题无法收款(40004)直接取消订单并返还所有钱到回收人员
                    if (!(order.getTitle() + "").equals(Order.TitleType.BIGTHING + "") && "40004".equals(response.getCode()) && order.getStatus() == Order.OrderType.ALREADY) {
                        String reOutBizNo = order.getOrderNo() + payment.getId();
                        AlipayFundTransUniTransferResponse responseback = this.aliPayTransfer(reOutBizNo, payment.getBuyerId(), payment.getPrice(), "ALIPAY_USER_ID");
                        if ((responseback.isSuccess() && "10000".equals(responseback.getCode()))) {
                            //返还金额到回收人员支付宝订单号
                            payment1.setUpdateBy(reOutBizNo);
                            order.setStatus(Order.OrderType.CANCEL);
                            order.setRemarks("收款账号有问题无法收款，已取消订单！！");
                            orderService.updateById(order);
                        }
                    }
                    paymentErrorService.insertOrUpdate(paymentError);
                    asyncService.notifyDingDingPaymentError(order.getOrderNo(), response.getBody(), PaymentError.DING_DING_URL, PaymentError.DING_DING_SING, PaymentError.DING_DING_TEL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!(response.isSuccess() || "10000".equals(response.getCode()))) {
                    order.setDiscountPrice(new BigDecimal(orderBean.getDiscountPrice()));
                    orderService.updateById(order);
                }
                if (Order.OrderType.CANCEL.equals(order.getStatus())) {
                    payment1.setDelFlag("1");
                }
                payment1.setRemarks(response.getSubMsg());
                this.updateById(payment1);
            }
        }
    }

    //支付宝转账
    public static void main(String[] args) throws Exception {


        System.out.println(new BigDecimal("0.00").compareTo(BigDecimal.ZERO) == 0);

    }

    public AlipayFundTransUniTransferResponse aliPayTransfer(String outBizNo, String aliUserId, BigDecimal amount, String payeeType) throws AlipayApiException {
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(outBizNo);
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity(aliUserId);
        payeeInfo.setIdentityType(payeeType);
        model.setTransAmount(amount.setScale(2, BigDecimal.ROUND_DOWN).toString());
        model.setOrderTitle("垃圾分类回收(收呗)货款");
        model.setRemark("垃圾分类回收(收呗)货款");
        model.setPayeeInfo(payeeInfo);
        model.setBizScene("DIRECT_TRANSFER");
        request.setBizModel(model);
        return certAlipayClient.certificateExecute(request);

    }

    /**
     * 根据支付宝交易号查询该交易的详细信息
     *
     * @param tradeNo
     */
    @Override
    public AlipayTradeQueryResponse getAliPayment(String tradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(tradeNo);
        model.setTradeNo(tradeNo);
        request.setBizModel(model);

        AlipayTradeQueryResponse response = null;
        try {
            response = certAlipayClient.certificateExecute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用查询交易接口成功 : " + response.getBody());
        } else {
            System.out.println("调用查询交易接口失败 : " + response.getBody());
        }
        return response;
    }

    /**
     * 查询转账信息
     */
    @Override
    public AlipayFundTransOrderQueryResponse getTransfer(String paymentId) {
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"" + paymentId + "\"" +
                //"\"order_id\":\"20160627110070001502260006780837\"" +
                "  }");
        AlipayFundTransOrderQueryResponse response = null;
        try {
            response = certAlipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    @SneakyThrows
    @Override
    public AlipayFundTransUniTransferResponse receivingMoneyTransfer(String aliUserId, String price, String outBizNo) {

        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(outBizNo);
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity(aliUserId);
        payeeInfo.setIdentityType("ALIPAY_USER_ID");
        model.setTransAmount(price);
        model.setOrderTitle("码上回收现金红包活动");
        model.setRemark("码上回收现金红包活动");
        model.setPayeeInfo(payeeInfo);
        model.setBizScene("DIRECT_TRANSFER");
        request.setBizModel(model);
        return certAlipayClient.certificateExecute(request);
    }

    /**
     * 交易关闭
     *
     * @param outTradeNo
     * @return
     */
    @Override
    public AlipayTradeCloseResponse paymentCloseByTradeNo(String outTradeNo) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        //model.setTradeNo(tradeNo);
        model.setOutTradeNo(outTradeNo);
        request.setBizModel(model);
        AlipayTradeCloseResponse response = null;
        try {
            response = certAlipayClient.certificateExecute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        System.out.println(response.getBody());
        return response;
    }

    @Override
    public String iotPay(String hardwareCode, String orderNo, String price, String outTradeNo) {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        String sn = orderNo;
        String subject = "垃圾分类清运订单(收呗):" + sn;

        model.setBody(subject);
        model.setSubject(subject);
        model.setOutTradeNo(outTradeNo);
        model.setTimeoutExpress("2m");
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2017011905224137");
        model.setExtendParams(extendParams);
        model.setTotalAmount(price);
        model.setProductCode("QUICK_MSECURITY_PAY");
        /**
         * 多增加设备硬件编号（获取时，不用重新查，知道开启哪台设备箱门）
         */
        model.setPassbackParams(hardwareCode);
        request.setBizModel(model);
        request.setNotifyUrl("test.equip.mayishoubei.com/equipment/mqtt/notify/alipay");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = certAlipayClient.sdkExecute(request);
            return response.getBody();
        } catch (AlipayApiException e) {
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
    }

    /**
     * iot定金回退接口
     *
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/15 0015
     * @Param:
     * @return:
     */
    @SneakyThrows
    @Override
    public AlipayFundTransUniTransferResponse iotTransfer(String aliUserId, String price, String outBizNo) {
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(outBizNo);
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity(aliUserId);
        payeeInfo.setIdentityType("ALIPAY_USER_ID");
        model.setTransAmount(price);
        model.setOrderTitle("支付宝垃圾分类回收");
        model.setRemark("定金回退");
        model.setPayeeInfo(payeeInfo);
        model.setBizScene("DIRECT_TRANSFER");
        request.setBizModel(model);
        return certAlipayClient.certificateExecute(request);

    }

    @Override
    public Payment selectPayOneMinByOrderSn(String orderNo) {
        return baseMapper.selectPayOneMinByOrderSn(orderNo);
    }

    @Override
    @Transactional
    public void transferDemo(Payment payment) {
        System.out.println(JSONObject.toJSONString(payment));
        payment.setDelFlag("1");
        this.updateById(payment);
        System.out.println(JSONObject.toJSONString(payment));
    }


    @Override
    public void transfer(String aliUserId, String amount, String orderNo) {
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(orderNo);
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity(aliUserId);
        payeeInfo.setIdentityType("ALIPAY_USER_ID");
        model.setTransAmount(amount);
        model.setOrderTitle("垃圾分类回收(收呗)随机红包");
        model.setRemark("垃圾分类回收(收呗)随机红包");
        model.setPayeeInfo(payeeInfo);
        model.setBizScene("DIRECT_TRANSFER");
        request.setBizModel(model);
        AlipayFundTransUniTransferResponse alipayFundTransUniTransferResponse = null;
        try {
            alipayFundTransUniTransferResponse = certAlipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            asyncService.notifyDingDingPaymentError(orderNo, "转账红包异常",
                    PaymentError.DING_DING_URL, PaymentError.DING_DING_SING, PaymentError.DING_DING_TEL);
        }
        if (!alipayFundTransUniTransferResponse.isSuccess()) {
            asyncService.notifyDingDingPaymentError(orderNo, "转账红包异常--------：" + alipayFundTransUniTransferResponse.getBody(),
                    PaymentError.DING_DING_URL, PaymentError.DING_DING_SING, PaymentError.DING_DING_TEL);
        }

    }
}
