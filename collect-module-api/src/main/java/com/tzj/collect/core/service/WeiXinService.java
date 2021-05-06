package com.tzj.collect.core.service;

import java.util.Map;

public interface WeiXinService {


    Map<String,String> getXcxOauthByCode(String code);

    Map<String,Object> getUserByToken(String accessToken,String openId);

}
