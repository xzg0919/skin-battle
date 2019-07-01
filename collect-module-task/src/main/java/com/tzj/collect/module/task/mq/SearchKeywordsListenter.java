package com.tzj.collect.module.task.mq;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 查询消息监听器
 */
@Component
public class SearchKeywordsListenter implements ChannelAwareMessageListener {

    protected final static Logger logger = LoggerFactory.getLogger(SearchKeywordsListenter.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        logger.info("收到的消息： {}",new String(message.getBody()));

        //消息确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
