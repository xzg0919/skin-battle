package com.tzj.collect.core.service;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.taobao.api.response.AlibabaCharityUseractionSyncResponse;

public interface GongYiCharityService {

    AlibabaCharityUseractionSyncResponse.UserActionSyncResult gongYiCharityUser(String aliUserId);

    Long gongYiCharityCharityTime(String aliUserId);

    AlipaySystemOauthTokenResponse selectUserToken(String userCode);

}
