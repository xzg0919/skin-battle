package com.tzj.collect.core.service;


import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;

public interface AnsycMyslService {

    public AntMerchantExpandTradeorderSyncResponse updateForest(String orderId, String myslParam);
    AntMerchantExpandTradeorderSyncResponse  updateCansForest(String aliUserId, String outBizNo, long count, String type);

}
