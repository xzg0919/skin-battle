package com.tzj.collect.service;

import com.alipay.api.response.AlipayIserviceCognitiveClassificationFeedbackSyncResponse;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationWasteQueryResponse;
import org.springframework.scheduling.annotation.Async;

/**
 * @author sgmark
 * @create 2019-07-04 15:21
 **/
public interface AliFlcxService {
     AlipayIserviceCognitiveClassificationWasteQueryResponse returnTypeByPicOrVoice(String picUrl, String voiceString);
     @Async
     void returnTypeVoice(String voiceString);

     AlipayIserviceCognitiveClassificationFeedbackSyncResponse lexCheckFeedBack(String traceId, String imageUrl, String feedbackRubbish, String actionType);
}
