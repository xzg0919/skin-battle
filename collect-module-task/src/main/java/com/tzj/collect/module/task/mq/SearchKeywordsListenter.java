package com.tzj.collect.module.task.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 查询消息监听器
 */
@Component
public class SearchKeywordsListenter implements ChannelAwareMessageListener {

    protected final static Logger logger = LoggerFactory.getLogger(SearchKeywordsListenter.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String jsonMessage = new String(message.getBody(), "UTF-8");
        HashMap<String, Object> goodsPriceObject = JSON.parseObject(jsonMessage, HashMap.class);

        //把接收到的消息，保存到数据库








        //logger.info("收到的消息： {}",new String(message.getBody()));

        //消息确认
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);




        //重试，重新进入队列
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);

        //丢弃这条记录
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    }
}
