package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.common.http.PostTool;
import com.tzj.collect.core.service.WeiXinService;
import com.tzj.collect.core.utils.WXTools;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
public class WeiXinServiceImpl implements WeiXinService {


    @Override
    public Map<String,Object> getUserByToken(String accessToken,String openId) {
        System.out.println("请求的参数是 ："+ accessToken+"---------"+openId);
        String url = "https://api.weixin.qq.com/sns/userinfo";
        String param = "access_token="+accessToken+"&openid="+openId;
        Map<String, Object> resultMap = null;
        try {
            String body = PostTool.postB(url, param);
            resultMap = (Map<String, Object>) JSONObject.parse(body);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;

    }

    @Override
    public Map<String,String> getXcxOauthByCode(String code) {
        System.out.println("请求的参数是 ："+ code);
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String param = "appid="+ WXTools.appId +"&secret="+WXTools.appSecret+"&js_code="+code+"&grant_type=authorization_code";
        Map<String, String> resultMap = null;
        try {
            String body = PostTool.postB(url, param);
            resultMap = (Map<String, String>) JSONObject.parse(body);
            System.out.println("session_key : "+resultMap.get("session_key"));
            System.out.println("openid : "+resultMap.get("openid"));
            System.out.println("unionid : "+resultMap.get("unionid"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;

    }







}
