package com.tzj.collect.module.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.tzj")
public class TaskApplication {
    protected final static Logger logger = LoggerFactory.getLogger(TaskApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TaskApplication.class,args);

        synchronized (TaskApplication.class) {
            TaskApplication.class.wait();
        }
    }
}
