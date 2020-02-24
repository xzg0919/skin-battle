package com.tzj.fish.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 本代码提供签名鉴权模式下 MQ4IOT 客户端发送消息到 MQ4IOT 客户端的示例，其中初始化参数请根据实际情况修改
 * 签名模式即使用阿里云账号系统提供的 AccessKey 和 SecretKey 对每个客户端计算出一个独立的签名供客户端识别使用。
 * 对于实际业务场景使用过程中，考虑到私钥 SecretKey 的隐私性，可以将签名过程放在受信任的环境完成。
 *
 * 完整 demo 工程，参考https://github.com/AliwareMQ/lmq-demo
 */
public class MQ4IoTSendMessageToMQ4IoTUseSignatureMode {
    public static void main(String[] args) throws Exception {
        /**
         * MQ4IOT 实例 ID，购买后控制台获取
         */
        String instanceId = "mqtt-cn-mp91j6h9z04";
        /**
         * 接入点地址，购买 MQ4IOT 实例，且配置完成后即可获取，接入点地址必须填写分配的域名，不得使用 IP 地址直接连接，否则可能会导致客户端异常。
         */
        String endPoint = "mqtt-cn-mp91j6h9z04.mqtt.aliyuncs.com";
        /**
         * 账号 accesskey，从账号系统控制台获取
         */
        String accessKey = "LTAI4FnZDKsBp4Yj5cL1rTbp";
        /**
         * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
         */
        String secretKey = "9H3yIgAl3xe9jvZ6R7RR7ie4oXb9Ti";
        /**
         * MQ4IOT clientId，由业务系统分配，需要保证每个 tcp 连接都不一样，保证全局唯一，如果不同的客户端对象（tcp 连接）使用了相同的 clientId 会导致连接异常断开。
         * clientId 由两部分组成，格式为 GroupID@@@DeviceId，其中 groupId 在 MQ4IOT 控制台申请，DeviceId 由业务方自己设置，clientId 总长度不得超过64个字符。
         */
        String clientId = "GID-TEST@@@XANYU-1";
        /**
         * MQ4IOT 消息的一级 topic，需要在控制台申请才能使用。
         * 如果使用了没有申请或者没有被授权的 topic 会导致鉴权失败，服务端会断开客户端连接。
         */
        final String parentTopic = "XANYU_TEST";
        /**
         * MQ4IOT支持子级 topic，用来做自定义的过滤，此处为示意，可以填写任何字符串，具体参考https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
         * 需要注意的是，完整的 topic 长度不得超过128个字符。
         */
        final String mq4IotTopic = parentTopic + "/" + "testMq4Iot";
        /**
         * QoS参数代表传输质量，可选0，1，2，根据实际需求合理设置，具体参考 https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
         */
        final int qosLevel = 1;
        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(instanceId, accessKey, secretKey, clientId);
        final MemoryPersistence memoryPersistence = new MemoryPersistence();
        /**
         * 客户端使用的协议和端口必须匹配，具体参考文档 https://help.aliyun.com/document_detail/44866.html?spm=a2c4g.11186623.6.552.25302386RcuYFB
         * 如果是 SSL 加密则设置ssl://endpoint:8883
         */
        final MqttClient mqttClient = new MqttClient("tcp://" + endPoint + ":1883", clientId, memoryPersistence);
        /**
         * 客户端设置好发送超时时间，防止无限阻塞
         */
        mqttClient.setTimeToWait(5000);

        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());



            MqttMessage message = new MqttMessage("hello word".getBytes());
            //message.setQos(qosLevel);
            /**
             *  发送普通消息时，topic 必须和接收方订阅的 topic 一致，或者符合通配符匹配规则
             */
           // mqttClient.publish(mq4IotTopic, message);
            /**
             * MQ4IoT支持点对点消息，即如果发送方明确知道该消息只需要给特定的一个设备接收，且知道对端的 clientId，则可以直接发送点对点消息。
             * 点对点消息不需要经过订阅关系匹配，可以简化订阅方的逻辑。点对点消息的 topic 格式规范是  {{parentTopic}}/p2p/{{targetClientId}}
             */
            final String p2pSendTopic = parentTopic + "/p2p/" +"GID-TEST@@@XANYU-2";
            message = new MqttMessage("hello mq4Iot p2p msg".getBytes());
            message.setQos(qosLevel);
            mqttClient.publish(p2pSendTopic, message);

        //Thread.sleep(Long.MAX_VALUE);
    }
}