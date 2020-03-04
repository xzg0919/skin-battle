package com.tzj.fish.config;

import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.internal.toplink.LinkException;
import com.taobao.api.request.TmcUserPermitRequest;
import com.taobao.api.response.TmcUserPermitResponse;
import com.tzj.fish.common.rocket.RocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 天猫消息监听
 * @author sgmark
 * @create 2020-01-06 11:27
 **/
@Configuration
public class TmcMessageListener {
    @Autowired
    private Producer producer;

    @Bean
    public TmcClient tmcClientListener(){
        TmcClient client = new TmcClient("28326416", "6b8fb4524e18b154851fa46c67768ef3", "default"); // 关于default参考消息分组说明
        client.setMessageHandler(new MessageHandler() {
            @Override
            public void onMessage(Message message, MessageStatus status) {
                try {
                    System.out.println(message.getContent());
                    System.out.println(message.getTopic());
                    //client.Send("helloworld-topic", "{helloworld-content}", "session_key");

                    com.aliyun.openservices.ons.api.Message aliMessage = new com.aliyun.openservices.ons.api.Message(RocketUtil.ALI_TOPIC,"TagA","Hello MQ".getBytes());
                    aliMessage.setKey(message.getContent());
                    // 发送消息，只要不抛异常就是成功
                    SendResult sendResult = producer.send(aliMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                    status.fail(); // 消息处理失败回滚，服务端需要重发
                    // 重试注意：不是所有的异常都需要系统重试。
                    // 对于字段不全、主键冲突问题，导致写DB异常，不可重试，否则消息会一直重发
                    // 对于，由于网络问题，权限问题导致的失败，可重试。
                    // 重试时间 5分钟不等，不要滥用，否则会引起雪崩
                }
            }
        });
        try {
            client.connect("ws://mc.api.taobao.com"); // 消息环境地址：ws://mc.api.tbsandbox.com/
        } catch (LinkException e) {
            e.printStackTrace();
        }
        return client;
    }
}
