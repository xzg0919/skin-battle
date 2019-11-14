package com.tzj.collect.core.service.impl;

import com.tzj.collect.common.http.PostTool;
import com.tzj.collect.common.constant.XingeConstant;
import com.tzj.collect.common.constant.XingeMessageCode;
import com.tzj.collect.core.service.XingeMessageService;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Michael_Wang
 *
 */
@Service
public class XingeMessageServiceImp implements XingeMessageService {

    /**
     * 腾讯信鸽推送消息
     *
     * @param title
     * @param content
     * @return
     */
    public void sendPostMessage(String title, String content, String device_token, XingeMessageCode code) {

        Long timestamp = System.currentTimeMillis() / 1000;
        String message_type = "1";
        Map<String, String> map = new HashMap<>();
        Map<String, String> mapConent = new HashMap<>();
        mapConent.put("code", code.getValue().toString());
        map.put("custom_content", JSONObject.fromObject(mapConent).toString());
        map.put("content", content);
        map.put("title", title);

        JSONObject jsonObject = JSONObject.fromObject(map);

        String message = jsonObject.toString();

        String params = "access_id=" + XingeConstant.access_id
                + "&timestamp=" + timestamp
                + "&device_token=" + device_token
                + "&message=" + message
                + "&message_type=" + message_type;

        String toSign = "POST" + XingeConstant.url
                + "access_id=" + XingeConstant.access_id
                + "device_token=" + device_token
                + "message=" + message
                + "message_type=" + message_type
                + "timestamp=" + timestamp
                + XingeConstant.secret_key;

        String sign = DigestUtils.md5Hex(toSign);
        params += "&sign=" + sign;

        PostTool.post("http://openapi.xg.qq.com/v2/push/single_device",
                params);

    }

    public static void main(String[] args) {
        new XingeMessageServiceImp().sendPostMessage("isManager", "1000", "f1222bb09e2f750d47e58b7968b87fea2eeb9c98", XingeMessageCode.cancelOrder);

//		/imp.sendPostMessage("title","您的订单已生成11","7eee54c561b5ceb34a93aea6c0af35d1ce490149",XingeMessageCode.order);
    }

}
