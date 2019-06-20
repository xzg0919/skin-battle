package com.tzj.collect.module.task.job;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Lazy(false)
public class UmengJob {

    protected final static Logger logger = LoggerFactory.getLogger(UmengJob.class);

    @Scheduled(initialDelay = 5000, fixedDelay = 3000)
    public void run() {
        logger.info("友盟JOB");
    }
}
