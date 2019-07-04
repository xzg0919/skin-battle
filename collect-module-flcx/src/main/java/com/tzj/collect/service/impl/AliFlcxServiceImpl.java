package com.tzj.collect.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.tzj.collect.api.commom.constant.AlipayConst;
import com.tzj.collect.service.AliFlcxService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author sgmark
 * @create 2019-07-04 15:30
 **/
@Service
public class AliFlcxServiceImpl implements AliFlcxService {
    @Override
    public AlipayResponse returnTypeByPicOrVoice(String picUrl, String voiceString) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.flcxaAppId,AlipayConst.flcx_private_key,AlipayConst.format,"utf-8",AlipayConst.flcx_ali_public_key,"RSA2");
//        AlipayIserviceCognitiveClassificationWasteQueryRequest request = new AlipayIserviceCognitiveClassificationWasteQueryRequest();
        AlipayResponse execute = null;
        try {
            BizContent bizContent = new BizContent();
            if (!StringUtils.isBlank(picUrl)){
                bizContent.setCognition_type(picUrl);
                bizContent.setCognition_type("ImageUrl");
            }else if (!StringUtils.isBlank(voiceString)){
                bizContent.setCognition_type(voiceString);
                bizContent.setCognition_type("SpeechText");
            }
//            request.setBizContent(JSON.toJSONString(bizContent));
//            execute = alipayClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execute;
    }
    @Data
    class BizContent{
        private String biz_code = "isv";//业务编码

        private String cognition_type = "Text";// 垃圾识别类型 (ImageUrl | SpeechText | Text)

        private String cognition_content;// 图片url，或者文本

        private String longitude;// 经度(否)

        private String latitude;//纬度(否)

        private String city_code;//市(否)
    }

}
