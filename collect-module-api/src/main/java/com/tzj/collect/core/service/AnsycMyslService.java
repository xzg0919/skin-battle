package com.tzj.collect.core.service;


import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.tzj.collect.core.param.mysl.MyslBean;

public interface AnsycMyslService {

      AntMerchantExpandTradeorderSyncResponse updateForest(String orderId, String myslParam, Integer times);
    AntMerchantExpandTradeorderSyncResponse  updateCansForest(String aliUserId, String outBizNo, long count, String type);
        AntMerchantExpandTradeorderSyncResponse updateCansForestByList(MyslBean myslBean);
}
