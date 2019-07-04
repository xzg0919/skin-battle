package com.tzj.collect.service;

import com.alipay.api.AlipayResponse;

/**
 * @author sgmark
 * @create 2019-07-04 15:21
 **/
public interface AliFlcxService {
    AlipayResponse returnTypeByPicOrVoice(String picUrl, String voiceString);
}
