package com.tzj.collect.module.task.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.service.FlcxRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * 查询消息监听器
 */
@Component
public class SearchKeywordsListenter implements ChannelAwareMessageListener {

    protected final static Logger logger = LoggerFactory.getLogger(SearchKeywordsListenter.class);

    @Autowired
    private FlcxRecordsService flcxRecordsService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String jsonMessage = new String(message.getBody(), "UTF-8");
        FlcxBean flcxBean = JSON.parseObject(jsonMessage, FlcxBean.class);

        //把接收到的消息，保存到数据库
        FlcxRecords flcxRecords=new FlcxRecords();
        flcxRecords.setAliUserId(flcxBean.getAliUserId());
        flcxRecords.setCity(flcxBean.getCity());    //如果不传，后面通过经纬度查
        flcxRecords.setCreateDate(new Date());
        flcxRecords.setUpdateDate(new Date());
        //flcxRecords.setLexiconAfter(flcxBean.);
        flcxRecordsService.saveFlcxRecords(flcxRecords);







        //logger.info("收到的消息： {}",new String(message.getBody()));

        //消息确认
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);




        //重试，重新进入队列
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);

        //丢弃这条记录
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    }
}
