package com.tzj.collect.common.constant;


import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;

/**
 * 消息队列常量
 */
public class RocketMqConst {


    public static final String TOPIC_ACCESS_ID="LTAIwQ0kfJaOky8n";

    public static final String TOPIC_ACCESS_KEY="tTDv8g3evSiFQSwjzMhjn5dSVvwIvI";

    public static final String TOPIC_URL="http://1804870195031869.mns.cn-shanghai.aliyuncs.com/";

    public static final String TOPIC_NAME_DELIVERY_ORDER="DeliveryOrder";

    public static final String TOPIC_NAME_RETURN_ORDER="ReturnOrder";

    public static final String DINGDING_ERROR = "https://oapi.dingtalk.com/robot/send?access_token=c41ce5b249d8627a88f0b9b00615fd55a5ce48d6a447a891eef25fe03e514cd9";

    //向socket发送消息
    public static void sendDeliveryOrder(String param,String topicName) {
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
//            System.out.println(msg.getMessageBodyMD5());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("rocketMq发送消息失败");
        }
        client.close();
    }


}
