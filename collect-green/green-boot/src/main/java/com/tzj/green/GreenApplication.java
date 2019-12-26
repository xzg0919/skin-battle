package com.tzj.green;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@ComponentScan("com.tzj")
@EnableCaching
@EnableAsync
@EnableScheduling
public class GreenApplication {

    protected final static Logger logger = LoggerFactory.getLogger(GreenApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GreenApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        logger.info("PortalApplication is success!");
    }

    @Bean
    public TomcatServletWebServerFactory webServerFactory() {

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {

            @Override
            public void customize(Connector connector) {
                connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
                connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
            }
        });
        return factory;
    }
}
