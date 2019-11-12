package com.tzj.collect.core.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyplsapi.model.v20170525.BindAxnRequest;
import com.aliyuncs.dyplsapi.model.v20170525.BindAxnResponse;
import com.aliyuncs.dyplsapi.model.v20170525.UnbindSubscriptionRequest;
import com.aliyuncs.dyplsapi.model.v20170525.UnbindSubscriptionResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.service.AliBindAxnService;
import org.springframework.stereotype.Service;
import shaded.com.google.gson.Gson;

import java.util.Date;


@Service
public class AliBindAxnServiceImpl implements AliBindAxnService {


    //返回隐私信息
    public BindAxnResponse getAxnPhone(String phoneA,String phoneB){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "<accessKeyId>", "<accessSecret>");
        IAcsClient client = new DefaultAcsClient(profile);

        BindAxnRequest request = new BindAxnRequest();
        request.setRegionId("cn-hangzhou");
        request.setPhoneNoA(phoneA);
        request.setExpiration(ToolUtils.getDateTimeToString(ToolUtils.addMinuteByNow(new Date(),30)));
        request.setPoolKey("fhvhgvghv");
        request.setPhoneNoB(phoneB);
        BindAxnResponse response = null;
        try {
            response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            System.out.println(response.getCode());
            System.out.println(response.getSecretBindDTO());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    //解除绑定关系
    public UnbindSubscriptionResponse deleteAnxPhone(String subsId,String secretNo){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "<accessKeyId>", "<accessSecret>");
        IAcsClient client = new DefaultAcsClient(profile);

        UnbindSubscriptionRequest request = new UnbindSubscriptionRequest();
        request.setRegionId("cn-hangzhou");
        request.setSubsId(subsId);
        request.setSecretNo(secretNo);
        request.setPoolKey("asa");
        UnbindSubscriptionResponse response = null;
        try {
            response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            System.out.println(response.getCode());
            System.out.println(response.getChargeId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


}
