package com.tzj.collect.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tzj.collect.api.commom.mqtt.MQTTConfig;
import com.tzj.collect.api.commom.mqtt.util.ConnectionOptionWrapper;
import com.tzj.collect.core.service.EquipmentMessageService;
import com.tzj.collect.core.service.OrderLogService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderLog;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.tzj.collect.api.commom.constant.MQTTConst.*;

/**
 * mqtt消息监听
 *
 * @author sgmark
 * @create 2019-11-15 10:50
 **/
@Configuration
public class MQTTMessageListener {

    @Resource
    private EquipmentMessageService messageService;
    @Resource
    private OrderService orderService;
    @Resource
    private OrderLogService orderLogService;

    protected final static Logger logger = LoggerFactory.getLogger(MQTTConfig.class);

    private String  instanceId = INSTANCE_ID;
    private String accessKey = ACCESS_KEY;
    private String secretKey = SECRET_KEY;
    private String clientId = "GID-IOT-MQTT@@@admin_collect";
    private String parentTopic = PARENT_TOPIC;
    private String endPoint = END_POINT;

    final String tokenServerUrl = "https://mqauth.aliyuncs.com";

    /**
     //     * 设置消息到达监听
     //     * @author: sgmark@aliyun.com
     //     * @Date: 2019/11/12 0012
     //     * @Param:
     //     * @return:
     //     */
    @Bean
    public MqttClient mqttClient() throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        final String mq4IotTopic = parentTopic + "/" + "admin_collect";
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
        final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                /**
                 * 客户端连接成功后就需要尽快订阅需要的 topic
                 */
                logger.info("the mqtt connect success");
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
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                /**
                 * 消费消息的回调接口，需要确保该接口不抛异常，该接口运行返回即代表消息消费成功。
                 * 消费消息需要保证在规定时间内完成，如果消费耗时超过服务端约定的超时时间，对于可靠传输的模式，服务端可能会重试推送，业务需要做好幂等去重处理。超时时间约定参考限制
                 * https://help.aliyun.com/document_detail/63620.html?spm=a2c4g.11186623.6.546.229f1f6ago55Fj
                 */
                Map<String, Object> mqttMessageMap = JSONObject.parseObject(new String(mqttMessage.getPayload()));
                if (CollectionUtils.isEmpty(mqttMessageMap)){
                    return;
                }
                String clientTopic = mqttMessageMap.get("clientTopic")+"";
                if (StringUtils.isEmpty(clientTopic)){
                    return;
                }
                logger.info("不要向我这边发消息了，我也是生产者，不处理消息");
                //处理客户端消息(处理下咸鱼回收的消息吧---我也很尴尬的啊---)

                /**
                 * 咸鱼回收流程
                 * 1、咸鱼推送聚石塔订单消息
                 * 2、聚石塔推送mqtt消息到收呗系统
                 * 3、系统保存订单信息
                 * 4、走相应的上门回收逻辑
                 * 5、系统完成订单
                 * 6、推送mqtt消息到聚石塔
                 * 7、调用履约接口完成咸鱼订单
                 */

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("send msg succeed topic is : " + iMqttDeliveryToken.getTopics()[0]);
            }
        });
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
        return mqttClient;
    }
    @Bean
    public MqttClient mqtt4PushOrder() throws MqttException, NoSuchAlgorithmException, InvalidKeyException {
        final String mq4OrderTopic = "shoubeiorder_topic/adc";
        /**
         * QoS参数代表传输质量，可选0，1，2，根据实际需求合理设置，具体参考 https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
         */
        final int qosLevel = 1;
        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(instanceId, accessKey, secretKey, "GID_ShouBeiOrder@@@test3");
        final MemoryPersistence memoryPersistence = new MemoryPersistence();
        /**
         * 客户端使用的协议和端口必须匹配，具体参考文档 https://help.aliyun.com/document_detail/44866.html?spm=a2c4g.11186623.6.552.25302386RcuYFB
         * 如果是 SSL 加密则设置ssl://endpoint:8883
         */
        final MqttClient mqttClient = new MqttClient("tcp://" + endPoint + ":1883", "GID_ShouBeiOrder@@@test3", memoryPersistence);
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
                logger.info("the mqtt connect success");
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String topicFilter[] = {mq4OrderTopic};
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
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                /**
                 * 消费消息的回调接口，需要确保该接口不抛异常，该接口运行返回即代表消息消费成功。
                 * 消费消息需要保证在规定时间内完成，如果消费耗时超过服务端约定的超时时间，对于可靠传输的模式，服务端可能会重试推送，业务需要做好幂等去重处理。超时时间约定参考限制
                 * https://help.aliyun.com/document_detail/63620.html?spm=a2c4g.11186623.6.546.229f1f6ago55Fj
                 */
                Map<String, Object> mqttMessageMap = JSONObject.parseObject(new String(mqttMessage.getPayload()));
                if (CollectionUtils.isEmpty(mqttMessageMap)){
                    return;
                }else{
                    Thread.sleep(100);
                    //收呗 收到推送信息并更改日志中版本为2.0 确保消息已发送
                    Object orderNo1 = mqttMessageMap.get("orderNo");
                    if (null == orderNo1){
                        return;
                    }
                    String orderNo = (String) orderNo1;
                    Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", orderNo));
                    if (null == order){
                        return;
                    }
                    Wrapper<OrderLog> orderLogWrapper = new EntityWrapper<OrderLog>().eq("order_id", order.getId());
                    OrderLog orderLog = null;
                    if(Order.OrderType.CANCEL.name().equals(order.getStatus().name())) {
                        orderLogWrapper.like("op_status_after", order.getStatus().name());
                    }else{
                        //取消订单，分用户和管理员端 有日志中记录取消时日志记录为 CANCELTASK 订单记录为CANCEL 情况
                        orderLogWrapper.eq("op_status_after", order.getStatus().name());
                    }
                    orderLog = orderLogService.selectOne(orderLogWrapper);
                    if (null == orderLog){
                        return;
                    }
                    orderLogService.updateById(orderLog);
                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("send msg succeed topic is : " + iMqttDeliveryToken.getTopics()[0]);
            }
        });
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
        return mqttClient;
    }
}
