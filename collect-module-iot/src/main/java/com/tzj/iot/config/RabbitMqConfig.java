package com.tzj.iot.config;

import com.tzj.iot.config.ExchangeConfig;
import com.tzj.iot.config.QueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EXCHANGE = "laji_search";

    public static final String SEARCH_KEYWORDS_QUEUE_ROUTE = "search_keywords_queue_route";

    @Autowired
    private QueueConfig queueConfig;

    @Autowired
    private ExchangeConfig exchangeConfig;

    /**
     * 连接工厂
     */
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 将消息队列1和交换机进行绑定
     */
    @Bean
    public Binding bindingSearchKeywordQueue() {
        return BindingBuilder.bind(queueConfig.searchKeywordsQueue()).to(exchangeConfig.directExchange()).with(RabbitMqConfig.SEARCH_KEYWORDS_QUEUE_ROUTE);
    }

    /**
     * 定义rabbit template用于数据的接收和发送
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
