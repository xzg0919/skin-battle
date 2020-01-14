package com.tzj.collect.core.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationFeedbackSyncResponse;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationWasteQueryResponse;
import com.tzj.collect.core.result.flcx.AlipayResponseResult;

/**
 * @author sgmark
 * @create 2019-07-04 15:21
 **/
public interface AliFlcxService {
     AlipayResponseResult returnTypeByPicOrVoice(String picUrl, String voiceString, String source);

     void returnTypeVoice(String voiceString);

     AlipayResponseResult lexCheckFeedBack(String traceId, String imageUrl, String feedbackRubbish, String actionType);
     //异步上传至标注平台
     void lexTagging(String lexName, String bizCode, String cityName) throws AlipayApiException;
}
