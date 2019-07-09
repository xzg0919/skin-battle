package com.tzj.collect.service;

import com.alipay.api.response.AlipayIserviceCognitiveClassificationFeedbackSyncResponse;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationWasteQueryResponse;

/**
 * @author sgmark
 * @create 2019-07-04 15:21
 **/
public interface AliFlcxService {
     AlipayIserviceCognitiveClassificationWasteQueryResponse returnTypeByPicOrVoice(String picUrl, String voiceString);

    AlipayIserviceCognitiveClassificationFeedbackSyncResponse lexCheckFeedBack(String traceId, String imageUrl, String feedbackRubbish, String actionType);
}
