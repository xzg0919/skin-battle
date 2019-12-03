package com.tzj.iot.api.mqtt;

import com.tzj.collect.api.commom.mqtt.util.Tools;
import com.tzj.collect.core.param.iot.MqttAppBean;
import com.tzj.collect.api.commom.mqtt.MQTTConfig;
import com.tzj.module.api.annotation.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.EQUIPMENT_APP_API_COMMON_AUTHORITY;


/**
 * iot数据通道
 *
 * @author sgmark
 * @create 2019-11-11 9:52
 **/
@ApiService
public class MQTTApi {
    @Resource
    private MQTTConfig mqttConfig;
    /**
     * 根据客户端信息返回mqtt的token
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/11 0011
     * @Param: 
     * @return: 
     */
    @Api(name = "mqtt.equipment.token", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public Map<String, Object>  mqttTokenByAppInfo(MqttAppBean mqttAppBean){
        if (StringUtils.isEmpty(mqttAppBean.getClientId())){
            throw new ApiException("clientId 为空");
        }
        List<String> resource = new ArrayList<String>();
        final String mq4IotTopic = mqttConfig.getParentTopic() + "/" + mqttAppBean.getClientId();
        resource.add(mq4IotTopic);
        Map<String, Object> returnMap = new HashMap<>();
        try {
            returnMap.put("token", Tools.applyToken(mqttConfig.getTokenServerUrl(), mqttConfig.getAccessKey(), mqttConfig.getSecretKey(), resource, "R,W", 2592000000L, mqttConfig.getInstanceId()));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        returnMap.put("instanceId", mqttConfig.getInstanceId());
        returnMap.put("accessKey", mqttConfig.getAccessKey());
        returnMap.put("endPoint", mqttConfig.getEndPoint());
        returnMap.put("clientId", mqttConfig.getClientId());
        return returnMap;
    }

}
