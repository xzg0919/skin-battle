package com.tzj.collect.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayOpenAppMiniTemplatemessageSendResponse;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.constant.AlipayConst;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.mapper.DailyMemberMapper;
import com.tzj.collect.mapper.DailyPaymentMapper;
import com.tzj.collect.module.common.shard.ShardTableHelper;
import com.tzj.collect.service.DailyMemberService;
import com.tzj.collect.service.DailyPaymentService;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

import static com.tzj.collect.common.constant.Const.*;
/**
 * 答答答memberService
 *
 * @author sgmark
 * @create 2019-08-29 9:35
 **/
@Service
@Transactional(readOnly = true)
public class DailyPaymentServiceImpl extends ServiceImpl<DailyPaymentMapper, Payment> implements DailyPaymentService {
    public  AlipayFundTransToaccountTransferResponse dailyDaTransfer(String aliUserId, String price, String outBizNo){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(outBizNo);
        model.setPayeeType("ALIPAY_USERID");
        model.setPayeeAccount(aliUserId);
        model.setAmount(price);
        model.setPayerShowName("答答答红包");
        model.setRemark("答答答红包");
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

    public static void main(String[] args) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(UUID.randomUUID().toString());
        model.setPayeeType("ALIPAY_USERID");
        model.setPayeeAccount("2088212384105273");
        model.setAmount("0.1");
        model.setPayerShowName("答答答红包");
        model.setRemark("答答答红包");
        request.setBizModel(model);
        AlipayFundTransToaccountTransferResponse response =null;
        try {
            response  = alipayClient.execute(request);
            System.out.println(response);
        }catch (AlipayApiException e){
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
    }

}
