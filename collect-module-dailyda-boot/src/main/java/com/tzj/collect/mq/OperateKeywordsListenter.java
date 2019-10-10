package com.tzj.collect.mq;

import com.alibaba.fastjson.JSON;
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
public class OperateKeywordsListenter implements ChannelAwareMessageListener {

    protected final static Logger logger = LoggerFactory.getLogger(OperateKeywordsListenter.class);


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        try {
            String jsonMessage = new String(message.getBody(), "UTF-8");
            Object flcxBean = JSON.parseObject(jsonMessage, Object.class);
            logger.info(flcxBean.toString());
        }catch (Exception e){
            //不管成功不成功，此条数据都丢弃
            e.printStackTrace();
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);






        //logger.info("收到的消息： {}",new String(message.getBody()));

        //消息确认
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);




        //重试，重新进入队列
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);

        //丢弃这条记录
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    }
}
