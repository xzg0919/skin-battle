package com.tzj.collect.flcx;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan("com.tzj")
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableDubbo(scanBasePackages = "com.tzj.collect")
@PropertySource(value = "classpath:/dubbo-config.properties")
public class FlcxApplicationBoot {

    protected final static Logger logger = LoggerFactory.getLogger(FlcxApplicationBoot.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FlcxApplicationBoot.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        logger.info("PortalApplication is success!");
    }
 
}
