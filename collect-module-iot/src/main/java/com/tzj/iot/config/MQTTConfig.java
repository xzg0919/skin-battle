package com.tzj.iot.config;

import com.tzj.iot.IoTApplication;
import com.tzj.iot.common.mqtt.util.ConnectionOptionWrapper;
import com.tzj.iot.common.mqtt.util.Tools;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sgmark
 * @create 2019-11-07 9:36
 **/
@Component
@Data
public class MQTTConfig  {

    protected final static Logger logger = LoggerFactory.getLogger(MQTTConfig.class);

    @Value("${mqtt-config.instanceId}")
    private String  instanceId;
    @Value("${mqtt-config.accessKey}")
    private String accessKey;
    @Value("${mqtt-config.secretKey}")
    private String secretKey;
    @Value("${mqtt-config.clientId}")
    private String clientId;
    @Value("${mqtt-config.parentTopic}")
    private String parentTopic;
    @Value("${mqtt-config.endPoint}")
    private String endPoint;

    final String tokenServerUrl = "https://mqauth.aliyuncs.com";
    /**
     * MQ4IOT支持子级 topic，用来做自定义的过滤，此处为示意，可以填写任何字符串，具体参考https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
     * 需要注意的是，完整的 topic 长度不得超过128个字符。
     */
    /**
     * Signature 鉴权模式下构造方法
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/7 0007
     * @Param:
     * @return:
     */
    @Bean("connectionOptionWrapperSignature")
    public ConnectionOptionWrapper ConnectionOptionWrapperSignatureBean(){
        ConnectionOptionWrapper connectionOptionWrapper = null;
        try {
            connectionOptionWrapper = new ConnectionOptionWrapper(instanceId, accessKey, secretKey, clientId);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return connectionOptionWrapper;
    }

    @Bean("connectionOptionWrapperToken")
    public ConnectionOptionWrapper ConnectionOptionWrapperTokenBean(){
        ConnectionOptionWrapper connectionOptionWrapper = null;
        List<String> resource = new ArrayList<String>();
        final String mq4IotTopic = parentTopic + "/" + "admin";
        resource.add(mq4IotTopic);
        /**
         * 此处示意，申请一个小时有效期的 token,实际使用时禁止在 MQTT 客户端程序申请 Token，以免引起 AccessKey，SecretKey 泄露，失去 token 的意义。
         */
        try {
            String token = Tools.applyToken(tokenServerUrl, accessKey, secretKey, resource, "R,W", 2592000000L, instanceId);
            Map<String, String> tokenData = new HashMap<String, String>();
            tokenData.put("RW", token);
            connectionOptionWrapper = new ConnectionOptionWrapper(instanceId, accessKey, clientId, tokenData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return connectionOptionWrapper;
    }
    /**
     * 设置消息到达监听
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/12 0012
     * @Param: 
     * @return: 
     */
    @Bean
    public void mqttMessageListener() throws MqttException, InvalidKeyException, NoSuchAlgorithmException {
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
                logger.info("mqtt connect success");
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String topicFilter[] = {mq4IotTopic};
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
                //处理业务逻辑
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("send msg succeed topic is : " + iMqttDeliveryToken.getTopics()[0]);
            }
        });
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
    }

}
