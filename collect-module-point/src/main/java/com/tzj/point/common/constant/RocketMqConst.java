package com.tzj.point.common.constant;


import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.tzj.module.common.aliyun.mns.Notification;
import com.tzj.module.common.aliyun.mns.XMLUtils;
import com.tzj.module.common.exception.BusiException;
import com.tzj.collect.common.notify.DingTalkNotify;
import org.apache.commons.lang3.StringUtils;

/**
 * 消息队列常量
 */
public class RocketMqConst {


    public static final String TOPIC_ACCESS_ID="LTAIwQ0kfJaOky8n";

    public static final String TOPIC_ACCESS_KEY="tTDv8g3evSiFQSwjzMhjn5dSVvwIvI";

    public static final String TOPIC_URL="http://1804870195031869.mns.cn-shanghai.aliyuncs.com/";

    public static final String TOPIC_NAME_DELIVERY_ORDER="DeliveryOrder";

    public static final String TOPIC_NAME_RETURN_ORDER="ReturnOrder";

    public static final String TOPIC_NAME_RETURN_ORDER_TEST="ReturnOeder-TEST";

    public static final String TOPIC_NAME_IOT_ORDER="IOT-TOPIC-TEST";

    public static final String DINGDING_ERROR = "https://oapi.dingtalk.com/robot/send?access_token=c41ce5b249d8627a88f0b9b00615fd55a5ce48d6a447a891eef25fe03e514cd9";

    //向socket发送消息
    public static void sendDeliveryOrder(String param,String topicName) {
        CloudAccount account = new CloudAccount(com.tzj.collect.common.mq.RocketMqConst.TOPIC_ACCESS_ID, com.tzj.collect.common.mq.RocketMqConst.TOPIC_ACCESS_KEY,
                com.tzj.collect.common.mq.RocketMqConst.TOPIC_URL);
        MNSClient client = account.getMNSClient(); // 在程序中，CloudAccount以及MNSClient单例实现即可，多线程安全
        CloudTopic topic = client.getTopicRef(topicName);
        try {
            TopicMessage msg = new RawTopicMessage() ; //可以使用TopicMessage结构，选择不进行Base64加密
            msg.setMessageBody(param);
            //msg.setMessageTag("filterTag"); //设置该条发布消息的filterTag
            msg = topic.publishMessage(msg);
            System.out.println("rocketMq发送消息成功 ："+msg.getMessageId()+" 内容是："+param);
//            System.out.println(msg.getMessageBodyMD5());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("rocketMq发送消息失败");
        }
        client.close();
    }

    /**
     * 预处理MNS收到的消息
     * @param body
     * @return
     */
    public static Notification processMNSMessage(String body, String topicName) throws BusiException {
        Notification notification=null;
        try {
            //解析XML,不能使用json
            notification = XMLUtils.parse(body);
        }catch(Exception e){
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"解析JSON出错！可能不是MNS消息",
                    com.tzj.collect.common.mq.RocketMqConst.DINGDING_ERROR,body);
            throw new BusiException("解析JSON出错！可能不是MNS消息");
        }
        if(!notification.getTopicName().equals(topicName)){
            //说明不是这个TOPIC的Message，不需要处理，直接return
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"topicName不一致！",
                    com.tzj.collect.common.mq.RocketMqConst.DINGDING_ERROR,topicName);
            throw new BusiException("topicName不一致！");
        }
        String messageId=notification.getMessageId();
        if(StringUtils.isBlank(messageId)){
            //说明不是MNS通知的，直接return
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"messageId不能为空！",
                    com.tzj.collect.common.mq.RocketMqConst.DINGDING_ERROR,messageId);
            throw new BusiException("messageId不能为空！");
        }
        return notification;
    }

}

