package com.tzj.collect.executor.core.config;


import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {
    public DirectExchange directExchange(){
        DirectExchange directExchange = new DirectExchange(RabbitMqConfig.EXCHANGE,true,false);
        return directExchange;
    }
}
