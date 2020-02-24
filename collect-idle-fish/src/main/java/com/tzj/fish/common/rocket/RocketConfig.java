package com.tzj.fish.common.rocket;

import com.aliyun.openservices.ons.api.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class RocketConfig {

    @Bean
    public Producer producer(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, RocketUtil.XANYU_GROUP_ID);
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey,RocketUtil.Ali_AccessKey);
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, RocketUtil.Ali_SecretKey);
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 设置 TCP 接入域名，到控制台的实例基本信息中查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR,
                RocketUtil.ALI_TCP_URL);
        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
        producer.start();
        return producer;
    }


    @Bean
    public Consumer consumer(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, RocketUtil.XANYU_GROUP_ID);
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey,RocketUtil.Ali_AccessKey);
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, RocketUtil.Ali_SecretKey);
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 设置 TCP 接入域名，到控制台的实例基本信息中查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR,
                RocketUtil.ALI_TCP_URL);
        // 集群订阅方式（默认）
        // properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        // 广播订阅方式
        // properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe(RocketUtil.ALI_TOPIC, "TagA||TagB", new MessageListener() { //订阅多个 Tag
            @Override
            public Action consume(Message message, ConsumeContext context) {
                System.out.println( message.getMsgID()+" : "+message.getKey());
                return Action.CommitMessage;
            }
        });
        //订阅另外一个 Topic
//        consumer.subscribe("XANYU_TEST", "*", new MessageListener() { //订阅全部 Tag
//            @Override
//            public Action consume(Message message, ConsumeContext context) {
//                System.out.println("Receive: " + message);
//                return Action.CommitMessage;
//            }
//        });
        consumer.start();
        return consumer;
    }

}
