package com.tzj.collect.api.common.mqtt;

import com.aliyun.openservices.ons.api.Action;
import com.tzj.collect.api.common.rocket.RocketUtil;
import com.tzj.collect.common.constant.ApplicaInit;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.core.service.RocketmqMessageService;
import com.tzj.collect.entity.RocketmqMessage;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class MQTTConfigs {

    @Autowired
    private RocketmqMessageService rocketmqMessageService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ApplicaInit applicaInit;

//    @Bean(value = "mqttClients")
//    public  MqttClient  mqttClients() throws Exception {
//        final MqttClient mqttClient = new MqttClient("tcp://" + MQTTUtil.END_POINT + ":1883", MQTTUtil.XIANYU_CLIENTID, new MemoryPersistence());
//        mqttClient.setTimeToWait(5000);//客户端设置好发送超时时间，防止无限阻塞
//        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(MQTTUtil.INSTANCE_ID, MQTTUtil.ACCESS_KEY, MQTTUtil.SECRET_KEY, MQTTUtil.XIANYU_CLIENTID);
//        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
//        return mqttClient;
//    }
    @Bean
    public String mqttConsumer(MqttClient mqttMessageListener){
        if (!applicaInit.getIsOpenXyConsumer()){
            return null;
        }
        final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        mqttMessageListener.setCallback(new MqttCallbackExtended() {
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
                            final String topicFilter[] = {MQTTUtil.PARENT_TOPIC};
                            final int[] qos = {MQTTUtil.QOS_LEVEL};
                            mqttMessageListener.subscribe(topicFilter, qos);
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
                String msg = new String( mqttMessage.getPayload());
                System.out.println("consumer topic " + s + " , 消息内容是 ： " + msg);
                try {
                    RocketmqMessage rocketmqMessage = new RocketmqMessage();
                    rocketmqMessage.setMessageId(s);
                    rocketmqMessage.setMessage(msg);
                    rocketmqMessage.setGroupId(RocketUtil.GROUP_ID);
                    rocketmqMessage.setTopicId(RocketUtil.ALI_TOPIC);
                    rocketmqMessageService.insert(rocketmqMessage);
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    orderService.saveXyOrder(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
        return null;
    }



}
