package com.tzj.module.common.notify.dingtalk;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;

public class DingTalkNotify {

    protected final static Logger logger = LoggerFactory.getLogger(DingTalkNotify.class);

    public static void sendAliErrorMessage(String className, String method, String body, String webHook, String bizContent) {
        try {
            String content = "class-------";
            content = content.concat(className);
            content = content.concat("\r");
            content = content.concat("method-------");
            content = content.concat(method);
            content = content.concat("\r");
            content = content.concat(body);
            content = content.concat("\r");
            content = content.concat("---------------------\r");
            if (null != bizContent) {
                content = content.concat(bizContent);
            }
            if (null != body && !body.contains("ACQ.TRADE_NOT_EXIST") && !body.contains("交易不存在")
                    && !body.contains("VOUCHER_ALREADY_OBTAINED") && !body.contains("领券超限")) {
                sendTextMessageWithAtAndAtAll(content, null, false, webHook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String content, String webhook) {
        sendTextMessageWithAtAndAtAll(content,null,false,webhook);
    }

    public static void sendTextMessageWithAt(String content,ArrayList<String> atMobiles,String webHook)  {
        sendTextMessageWithAtAndAtAll(content,atMobiles,false,webHook);
    }
    public static void sendTextMessageWithAtAll(String content,String webHook)  {
        sendTextMessageWithAtAndAtAll(content,null,true,webHook);
    }

    public static void sendTextMessageWithAtAndAtAll(String content, ArrayList<String> atMobiles, boolean atAll, String webhook) {
        HashMap<String, Object> message = new HashMap<>();
        HashMap<String, String> textContent = new HashMap<>();
        textContent.put("content", content);

        message.put("msgtype", "text");
        message.put("text", textContent);

        HashMap<String,Object> at=new HashMap<>();
        if(atMobiles!=null && atMobiles.size()>0){
            at.put("atMobiles",atMobiles);
        }
        at.put("isAtAll",atAll);
        message.put("at", at);

        new Thread(() -> {
            try {
                Response response = FastHttpClient.post().url(webhook).addHeader("Content-Type", "application/json").body(JSON.toJSONString(message)).build().execute();
                String resultJson = response.body().string();

                logger.info("钉钉：" + resultJson);
            } catch (Exception e) {
                logger.info("钉钉消息发送异常:" + e.getMessage());
            }
        });
    }


    public static void main(String args[]) {
        try {

            ArrayList<String> atMobiles=new ArrayList<>();
            atMobiles.add("18516291937");
            sendTextMessageWithAtAndAtAll("一个人测试", atMobiles,false,"https://oapi.dingtalk.com/robot/send?access_token=9125390abc8e864da7deddba297580b32948bd8752804f8d25cc524372f78fbd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
