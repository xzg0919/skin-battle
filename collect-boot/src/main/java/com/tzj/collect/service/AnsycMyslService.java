package com.tzj.collect.service;


import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.tzj.collect.api.ali.param.OrderBean;
import org.springframework.scheduling.annotation.Async;

public interface AnsycMyslService {

    public AntMerchantExpandTradeorderSyncResponse updateForest(String orderId,String myslParam);
    AntMerchantExpandTradeorderSyncResponse  updateCansForest(String aliUserId,String outBizNo,long count,String type);

}
