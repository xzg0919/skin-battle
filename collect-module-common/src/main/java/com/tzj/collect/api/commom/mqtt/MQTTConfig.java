package com.tzj.collect.api.commom.mqtt;

import com.tzj.collect.api.commom.mqtt.util.ConnectionOptionWrapper;
import com.tzj.collect.api.commom.mqtt.util.Tools;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.api.commom.constant.MQTTConst.*;

/**
 * @author sgmark
 * @create 2019-11-07 9:36
 **/
@Data
@Component
public class MQTTConfig  {

    protected final static Logger logger = LoggerFactory.getLogger(MQTTConfig.class);

    private String  instanceId = INSTANCE_ID;
    private String accessKey = ACCESS_KEY;
    private String secretKey = SECRET_KEY;
    private String clientId = CLIENT_ID;
    private String parentTopic = PARENT_TOPIC;
    private String endPoint = END_POINT;

    final String tokenServerUrl = "https://mqauth.aliyuncs.com";
    /**
     * MQ4IOT支持子级 topic，用来做自定义的过滤，此处为示意，可以填写任何字符串，具体参考https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
     * 需要注意的是，完整的 topic 长度不得超过128个字符。
     */
    /**
     * Signature 鉴权模式下构造方法
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/7 0007
     * @Param:
     * @return:
     */
    @Bean("connectionOptionWrapperSignature")
    public ConnectionOptionWrapper ConnectionOptionWrapperSignatureBean(){
        ConnectionOptionWrapper connectionOptionWrapper = null;
        try {
            connectionOptionWrapper = new ConnectionOptionWrapper(instanceId, accessKey, secretKey, clientId);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return connectionOptionWrapper;
    }
//    @Bean("connectionOptionWrapperToken")
//    public ConnectionOptionWrapper ConnectionOptionWrapperTokenBean(){
//         ConnectionOptionWrapper connectionOptionWrapper = null;
//        List<String> resource = new ArrayList<String>();
//        final String mq4IotTopic = parentTopic + "/" + "admin";
//        resource.add(mq4IotTopic);
//      /*  *
//         * 此处示意，申请一个小时有效期的 token,实际使用时禁止在 MQTT 客户端程序申请 Token，以免引起 AccessKey，SecretKey 泄露，失去 token 的意义。
//        */
//        try {
//            String token = Tools.applyToken(tokenServerUrl, accessKey, secretKey, resource, "R,W", 2592000000L, instanceId);
//            Map<String, String> tokenData = new HashMap<String, String>();
//            tokenData.put("RW", token);
//            connectionOptionWrapper = new ConnectionOptionWrapper(instanceId, accessKey, clientId, tokenData);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        return connectionOptionWrapper;
//    }
}
