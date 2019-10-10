package com.tzj.collect.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationFeedbackSyncResponse;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationWasteQueryResponse;

/**
 * @author sgmark
 * @create 2019-07-04 15:21
 **/
public interface AliFlcxService {
     AlipayIserviceCognitiveClassificationWasteQueryResponse returnTypeByPicOrVoice(String picUrl, String voiceString, String source);

     void returnTypeVoice(String voiceString);

     AlipayIserviceCognitiveClassificationFeedbackSyncResponse lexCheckFeedBack(String traceId, String imageUrl, String feedbackRubbish, String actionType);
     //异步上传至标注平台
     void lexTagging(String lexName, String bizCode, String cityName) throws AlipayApiException;
}
