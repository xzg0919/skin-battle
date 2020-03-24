package com.tzj.fish.common.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class MQTTConfigs {

    @Bean(value = "mqttClients")
    public  MqttClient  mqttClients() throws Exception {
        final MqttClient mqttClient = new MqttClient("tcp://" + MQTTUtil.END_POINT + ":1883", MQTTUtil.XIANYU_CLIENTID, new MemoryPersistence());
        mqttClient.setTimeToWait(5000);//客户端设置好发送超时时间，防止无限阻塞
        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(MQTTUtil.INSTANCE_ID, MQTTUtil.ACCESS_KEY, MQTTUtil.SECRET_KEY, MQTTUtil.XIANYU_CLIENTID);
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
        return mqttClient;
    }
    //@Bean
    public String consumer(MqttClient mqttClients){
        final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        mqttClients.setCallback(new MqttCallbackExtended() {
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
                            final String topicFilter[] = {MQTTUtil.XIANYU_MQ4IOTTOPIC};
                            final int[] qos = {MQTTUtil.QOS_LEVEL};
                            mqttClients.subscribe(topicFilter, qos);
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
                System.out.println("consumer topic " + s + " , 消息内容是 ： " + new String( mqttMessage.getPayload()));
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("sssss : " + iMqttDeliveryToken.getTopics()[0]);
            }
        });
        return null;
    }



}
