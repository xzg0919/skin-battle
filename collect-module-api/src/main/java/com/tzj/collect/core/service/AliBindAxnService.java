package com.tzj.collect.core.service;

import com.aliyuncs.dyplsapi.model.v20170525.BindAxnResponse;
import com.aliyuncs.dyplsapi.model.v20170525.UnbindSubscriptionResponse;

public interface AliBindAxnService {


    public BindAxnResponse getAxnPhone(String phoneNoA,String phoneNoB);

    public UnbindSubscriptionResponse deleteAnxPhone(String subsId, String secretNo);
}
