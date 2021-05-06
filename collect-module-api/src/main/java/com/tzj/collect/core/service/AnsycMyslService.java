package com.tzj.collect.core.service;


import com.alipay.api.response.AlipayEcoActivityRecycleSendResponse;
import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.tzj.collect.core.param.mysl.MyslBean;

public interface AnsycMyslService {

    AlipayEcoActivityRecycleSendResponse updateForest(String orderId, String myslParam, Integer times);
    AlipayEcoActivityRecycleSendResponse  updateCansForest(String aliUserId, String outBizNo, long count, String type);
    AlipayEcoActivityRecycleSendResponse updateCansForestByList(MyslBean myslBean);
}
