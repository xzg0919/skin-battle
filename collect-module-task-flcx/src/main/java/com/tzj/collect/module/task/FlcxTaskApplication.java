package com.tzj.collect.module.task;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@EnableCaching
@EnableScheduling
@ComponentScan("com.tzj")
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class FlcxTaskApplication {
    protected final static Logger logger = LoggerFactory.getLogger(FlcxTaskApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(FlcxTaskApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        logger.info("FlcxTaskApplication is success!");
    }
}
