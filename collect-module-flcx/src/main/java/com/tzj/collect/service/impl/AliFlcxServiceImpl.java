package com.tzj.collect.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayIserviceCognitiveClassificationCategorySyncRequest;
import com.alipay.api.request.AlipayIserviceCognitiveClassificationFeedbackSyncRequest;
import com.alipay.api.request.AlipayIserviceCognitiveClassificationWasteQueryRequest;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationCategorySyncResponse;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationFeedbackSyncResponse;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationWasteQueryResponse;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.service.AliFlcxService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author sgmark
 * @create 2019-07-04 15:30
 *
 */
@Service
public class AliFlcxServiceImpl implements AliFlcxService {

    public AlipayIserviceCognitiveClassificationWasteQueryResponse returnTypeByPicOrVoice(String picUrl, String voiceString, String source) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.flcxaAppId, AlipayConst.flcx_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.flcx_ali_public_key, AlipayConst.sign_type);
        AlipayIserviceCognitiveClassificationWasteQueryRequest request = new AlipayIserviceCognitiveClassificationWasteQueryRequest();
        AlipayIserviceCognitiveClassificationWasteQueryResponse execute = null;
        try {
            BizContent bizContent = new BizContent();
            if (!StringUtils.isBlank(picUrl)) {
                bizContent.setSource(source);
                bizContent.setCognition_content(picUrl);
                bizContent.setCognition_type("ImageUrl");
            } else if (!StringUtils.isBlank(voiceString)) {
                bizContent.setCognition_content(voiceString);
                bizContent.setCognition_type("SpeechText");
            }
            request.setBizContent(JSON.toJSONString(bizContent));

            execute = alipayClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execute;
    }

    public void returnTypeVoice(String voiceString) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.flcxaAppId, AlipayConst.flcx_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.flcx_ali_public_key, AlipayConst.sign_type);
        AlipayIserviceCognitiveClassificationWasteQueryRequest request = new AlipayIserviceCognitiveClassificationWasteQueryRequest();
        AlipayIserviceCognitiveClassificationWasteQueryResponse execute = null;
        try {
            BizContent bizContent = new BizContent();
            bizContent.setCognition_content(voiceString);
            bizContent.setCognition_type("SpeechText");
            request.setBizContent(JSON.toJSONString(bizContent));
            execute = alipayClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AlipayIserviceCognitiveClassificationFeedbackSyncResponse lexCheckFeedBack(String traceId, String imageUrl, String feedbackRubbish, String actionType) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.flcxaAppId, AlipayConst.flcx_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.flcx_ali_public_key, AlipayConst.sign_type);
        AlipayIserviceCognitiveClassificationFeedbackSyncRequest request = new AlipayIserviceCognitiveClassificationFeedbackSyncRequest();
        AlipayIserviceCognitiveClassificationFeedbackSyncResponse execute = null;
        try {
            BizContent bizContent = new BizContent();
            if (!StringUtils.isBlank(imageUrl)) {
                bizContent.setCognition_content(imageUrl);
                bizContent.setCognition_type("ImageUrl");
            }
            bizContent.setTrace_id(traceId);
            bizContent.setFeedback_rubbish(feedbackRubbish);
            bizContent.setAction_type(StringUtils.isBlank(actionType) || "feedback_rectify".equals(actionType) ? "feedback_rectify" : actionType);//修正或忽略
            request.setBizContent(JSON.toJSONString(bizContent));
            execute = alipayClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execute;
    }

    @Data
    static class BizContent {

        private String biz_code = "isv";//业务编码

        private String cognition_type = "Text";// 垃圾识别类型 (ImageUrl | SpeechText | Text)

        private String cognition_content;// 图片url，或者文本

        private String longitude;// 经度(否)

        private String latitude;//纬度(否)

        private String city_code;//市(否)

        private String trace_id;//反馈id

        private String feedback_rubbish;//反馈名称

        private String action_type;

        private String source;//区分ar/isv
    }

    public static void main(String[] args) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.flcxaAppId, AlipayConst.flcx_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.flcx_ali_public_key, AlipayConst.sign_type);
        AlipayIserviceCognitiveClassificationWasteQueryRequest request = new AlipayIserviceCognitiveClassificationWasteQueryRequest();
        AlipayIserviceCognitiveClassificationWasteQueryResponse execute = null;
        try {
            BizContent bizContent = new BizContent();
            bizContent.setCognition_content("卡片纸很漂亮");
            bizContent.setCognition_type("SpeechText");
            request.setBizContent(JSON.toJSONString(bizContent));
            execute = alipayClient.execute(request);
            System.out.println(execute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AlipayIserviceCognitiveClassificationWasteQueryResponse re(String picUrl, String voiceString) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.flcxaAppId, AlipayConst.flcx_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.flcx_ali_public_key, AlipayConst.sign_type);
        AlipayIserviceCognitiveClassificationWasteQueryRequest request = new AlipayIserviceCognitiveClassificationWasteQueryRequest();
        AlipayIserviceCognitiveClassificationWasteQueryResponse execute = null;
        try {
            BizContent bizContent = new BizContent();
            if (!StringUtils.isBlank(picUrl)) {
                bizContent.setCognition_content(picUrl);
                bizContent.setCognition_type("ImageUrl");
            } else if (!StringUtils.isBlank(voiceString)) {
                bizContent.setCognition_content(voiceString);
                bizContent.setCognition_type("SpeechText");
            }
            request.setBizContent("{"
                    + "\"biz_code\":\"isv\","
                    + "\"cognition_type\":\"ImageUrl\","
                    + "\"cognition_content\":\"http://images.sqmall.top/collect/20190709/original_c4f39332-6608-41e1-8a4e-b4a2c016faa0.jpg\","
                    + "\"longitude\":\"121.4411213954\","
                    + "\"latitude\":\"31.2121751783\","
                    + "\"city_code\":\"city\""
                    + "  }");
            execute = alipayClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(execute);
        return execute;
    }

    public static AlipayResponse pe(String picUrl, String traceId, String feedbackRubbish) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.flcxaAppId, AlipayConst.flcx_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.flcx_ali_public_key, AlipayConst.sign_type);
        AlipayIserviceCognitiveClassificationFeedbackSyncRequest request = new AlipayIserviceCognitiveClassificationFeedbackSyncRequest();
        AlipayIserviceCognitiveClassificationFeedbackSyncResponse execute = null;
        try {
            BizContent bizContent = new BizContent();
            if (!StringUtils.isBlank(picUrl)) {
                bizContent.setCognition_content(picUrl);
                bizContent.setCognition_type("ImageUrl");
            }
            bizContent.setTrace_id(traceId);
            bizContent.setFeedback_rubbish(feedbackRubbish);
            request.setBizContent(JSON.toJSONString(bizContent));
            execute = alipayClient.execute(request);
            System.out.println(execute);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execute;
    }

    /**
     * 上传至支付宝标注平台
     *
     * @author sgmark@aliyun.com
     * @date 2019/7/30 0030
     * @param
     * @return
     */
    public void lexTagging(String lexName, String bizCode, String cityName) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.flcxaAppId, AlipayConst.flcx_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.flcx_ali_public_key, AlipayConst.sign_type);
        AlipayIserviceCognitiveClassificationCategorySyncRequest request = new AlipayIserviceCognitiveClassificationCategorySyncRequest();
        request.setBizContent("{"
                + "\"biz_code\":\"" + bizCode + "\","
                + " \"rubbish_list\":[{"
                + " \"key_word\":\"" + lexName + "\","
                + "\"category\":\"dry\""
                + " }],"
                + "\"city_code\":\"" + cityName + "\""
                + " }");
        AlipayIserviceCognitiveClassificationCategorySyncResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}
