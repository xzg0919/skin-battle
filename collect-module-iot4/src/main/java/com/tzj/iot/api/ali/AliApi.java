package com.tzj.iot.api.ali;

import com.tzj.collect.api.commom.constant.MQTTConst;
import com.tzj.collect.common.security.MD5Util;
import com.tzj.collect.core.param.iot.IOT4Bean;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.entity.Member;
import com.tzj.iot.common.mqtt.MqttCommon;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020/11/20 10:46
 * @Description:
 */
@ApiService
public class AliApi {

    private static final String SIGN_KEY = "40ec20dab72b49d98e876429358ab24c";

    @Autowired
    MqttClient mqttClient;

    @Autowired
    MemberService memberService;

    /**
     * 扫码开箱
     *
     * @return
     */
    @Api(name = "scanCode.OpenTheBox", version = "1.0")
    @SignIgnore
    @AuthIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> qrCode(IOT4Bean iot4Bean) {
        Map<String, Object> returnMap = new HashMap<>();
        Long timeStamp = iot4Bean.getTimeStamp();
        String deviceCode = iot4Bean.getDeviceCode();
        String sign = iot4Bean.getSign();


        //校验参数
        if (MD5Util.encode(deviceCode + timeStamp + SIGN_KEY).equals(sign)) {
            Member member  = memberService.findMemberByAliId(iot4Bean.getAliUserId());
            if(member ==null){
                throw new ApiException("用户不存在！");
            }

            //生成用户token
            String token = JwtUtils.generateToken(member.getAliUserId(), EQUIPMENT_APP_API_EXPRIRE, EQUIPMENT_APP_API_TOKEN_SECRET_KEY);
            String securityToken = JwtUtils.generateEncryptToken(token, EQUIPMENT_APP_API_TOKEN_CYPTO_KEY);
            TokenBean tokenBean = new TokenBean();
            tokenBean.setExpire(EQUIPMENT_APP_API_EXPRIRE);
            tokenBean.setToken(securityToken);

            //发送用户信息
            MqttMessage mqttMessage = new MqttMessage(MqttCommon.convertToJson(MqttCommon.mqttType.IDENTIFY_USERS,tokenBean,deviceCode).toString().getBytes());
            mqttMessage.setQos(1);
            try {
                mqttClient.publish(MQTTConst.PARENT_TOPIC + "/" + "iot4", mqttMessage);
            } catch (MqttException e) {
                e.printStackTrace();
                throw new ApiException("箱门开启失败，请稍后再试！");
            }
        } else {
            throw new ApiException("二维码无效！");
        }
        returnMap.put("msg", "箱门已打开");
        return returnMap;

    }







}
