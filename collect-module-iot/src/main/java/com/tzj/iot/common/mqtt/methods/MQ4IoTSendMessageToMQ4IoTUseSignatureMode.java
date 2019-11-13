package com.tzj.iot.common.mqtt.methods;

import com.tzj.iot.common.mqtt.util.ConnectionOptionWrapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * mqtt客户端发送到客户端消息
 *
 * @author sgmark
 * @create 2019-11-06 10:29
 **/
public class MQ4IoTSendMessageToMQ4IoTUseSignatureMode {

    final int qosLevel = 0;
    @Resource(name = "connectionOptionWrapperSignature")
    private ConnectionOptionWrapper connectionOptionWrapperSignature;
    @Value("${mqtt-config.instanceId}")
    private String  instanceId;
    @Value("${mqtt-config.clientId}")
    private String clientId;
    @Value("${mqtt-config.parentTopic}")
    private String parentTopic;

    public void sendMessageToMQ4IoTUseSignatureMode(String message, String sendTo) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(qosLevel);
        final MemoryPersistence memoryPersistence = new MemoryPersistence();
        /**
         * 客户端使用的协议和端口必须匹配，具体参考文档 https://help.aliyun.com/document_detail/44866.html?spm=a2c4g.11186623.6.552.25302386RcuYFB
         * 如果是 SSL 加密则设置ssl://endpoint:8883
         */
        final MqttClient mqttClient = new MqttClient("tcp://" +  instanceId + ":1883", clientId, memoryPersistence);
        mqttClient.connect(connectionOptionWrapperSignature.getMqttConnectOptions());
        /**
         * 客户端设置好发送超时时间，防止无限阻塞
         */
        mqttClient.setTimeToWait(5000);
        mqttClient.publish(parentTopic + "/" + sendTo, mqttMessage);
    }




    public static void main(String[] args) throws Exception {
        /**
         * MQ4IOT 实例 ID，购买后控制台获取
         */
        String instanceId = "post-cn-4591dzam925";
        /**
         * 接入点地址，购买 MQ4IOT 实例，且配置完成后即可获取，接入点地址必须填写分配的域名，不得使用 IP 地址直接连接，否则可能会导致客户端异常。
         */
        String endPoint = "post-cn-4591dzam925.mqtt.aliyuncs.com";
        /**
         * 账号 accesskey，从账号系统控制台获取
         */
        String accessKey = "LTAIMbbuj3E2uX48";
        /**
         * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
         */
        String secretKey = "V8RPkZqqaBg6QK0mk9GsPcub8ePRyN";
        /**
         * MQ4IOT clientId，由业务系统分配，需要保证每个 tcp 连接都不一样，保证全局唯一，如果不同的客户端对象（tcp 连接）使用了相同的 clientId 会导致连接异常断开。
         * clientId 由两部分组成，格式为 GroupID@@@DeviceId，其中 groupId 在 MQ4IOT 控制台申请，DeviceId 由业务方自己设置，clientId 总长度不得超过64个字符。
         */
        String clientId = "GID-IOT-MQTT@@@SH0066";
        /**
         * MQ4IOT 消息的一级 topic，需要在控制台申请才能使用。
         * 如果使用了没有申请或者没有被授权的 topic 会导致鉴权失败，服务端会断开客户端连接。
         */
        final String parentTopic = "iot_topic";
        /**
         * MQ4IOT支持子级 topic     ，用来做自定义的过滤，此处为示意，可以填写任何字符串，具体参考https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
         * 需要注意的是，完整的 topic 长度不得超过128个字符。
         */
        final String mq4IotTopic = parentTopic + "/" + "admin";
        /**
         * QoS参数代表传输质量，可选0，1，2，根据实际需求合理设置，具体参考 https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
         */
        final int qosLevel = 0;
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
        final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                /**
                 * 客户端连接成功后就需要尽快订阅需要的 topic
                 */
                System.out.println("connect success");
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String topicFilter[] = {parentTopic + "/" + "SH0066"};
                            final int[] qos = {qosLevel};
                            mqttClient.subscribe(topicFilter, qos);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void connectionLost(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                /**
                 * 消费消息的回调接口，需要确保该接口不抛异常，该接口运行返回即代表消息消费成功。
                 * 消费消息需要保证在规定时间内完成，如果消费耗时超过服务端约定的超时时间，对于可靠传输的模式，服务端可能会重试推送，业务需要做好幂等去重处理。超时时间约定参考限制
                 * https://help.aliyun.com/document_detail/63620.html?spm=a2c4g.11186623.6.546.229f1f6ago55Fj
                 */
                System.out.println(
                        "receive msg from topic " + s + " , body is " + new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("send msg succeed topic is : " + iMqttDeliveryToken.getTopics()[0]);
            }
        });
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
        MqttMessage message = new MqttMessage("hello mq4Iot pub sub msg".getBytes());
        message.setQos(qosLevel);
        /**
         *  发送普通消息时，topic 必须和接收方订阅的 topic 一致，或者符合通配符匹配规则
         */
        mqttClient.publish(mq4IotTopic, message);
        /**
         * MQ4IoT支持点对点消息，即如果发送方明确知道该消息只需要给特定的一个设备接收，且知道对端的 clientId，则可以直接发送点对点消息。
         * 点对点消息不需要经过订阅关系匹配，可以简化订阅方的逻辑。点对点消息的 topic 格式规范是  {{parentTopic}}/p2p/{{targetClientId}}
         */
        final String p2pSendTopic = parentTopic + "/p2p/" + "admin";
        message = new MqttMessage("hello mq4Iot p2p msg".getBytes());
        message.setQos(qosLevel);
        mqttClient.publish(p2pSendTopic, message);
        Thread.sleep(Long.MAX_VALUE);
    }
}
