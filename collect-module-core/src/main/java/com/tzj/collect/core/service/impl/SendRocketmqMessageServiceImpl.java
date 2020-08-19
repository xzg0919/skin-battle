package com.tzj.collect.core.service.impl;


import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.mq.RocketMqConst;
import com.tzj.collect.core.mapper.SendRocketmqMessageMapper;
import com.tzj.collect.core.service.SendRocketmqMessageService;
import com.tzj.collect.entity.SendRocketmqMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class SendRocketmqMessageServiceImpl extends ServiceImpl<SendRocketmqMessageMapper, SendRocketmqMessage> implements SendRocketmqMessageService {

        //向socket发送消息
        @Transactional
        @Override
        public void sendDeliveryOrder(String param,String topicName) {
                CloudAccount account = new CloudAccount(RocketMqConst.TOPIC_ACCESS_ID, RocketMqConst.TOPIC_ACCESS_KEY,
                        RocketMqConst.TOPIC_URL);
                MNSClient client = account.getMNSClient(); // 在程序中，CloudAccount以及MNSClient单例实现即可，多线程安全
                CloudTopic topic = client.getTopicRef(topicName);
                try {
                        TopicMessage msg = new RawTopicMessage() ; //可以使用TopicMessage结构，选择不进行Base64加密
                        //String jsonStr="{\"aliUId\":null,\"member_id\":\"330227\",\"user_code\":\"010031357626\",\"level\":\"2\",\"name\":\"hhh\",\"valid_level_time\":\"2019-12-31 23:59:59\"}";
                        msg.setMessageBody(param);
                        //msg.setMessageTag("filterTag"); //设置该条发布消息的filterTag
                        msg = topic.publishMessage(msg);
                        System.out.println("rocketMq发送消息成功 ："+msg.getMessageId()+" 内容是："+param);
                        SendRocketmqMessage sendRocketmqMessage = new SendRocketmqMessage();
                        sendRocketmqMessage.setMessageId(msg.getMessageId());
                        sendRocketmqMessage.setMessage(param+"---topicName : "+topicName);
                        this.insert(sendRocketmqMessage);
//            System.out.println(msg.getMessageBodyMD5());
                } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("rocketMq发送消息失败");
                }
                client.close();
        }
}
