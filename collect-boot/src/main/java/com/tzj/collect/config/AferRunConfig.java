package com.tzj.collect.config;

import com.tzj.collect.commom.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/9/16 14:55
 * @Description:
 */
@Slf4j
@Component
public class AferRunConfig implements ApplicationRunner {
    @Resource
    private RedisUtil redisUtil;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> allowDay = new ArrayList<>();
        List<String> recyclers = new ArrayList<>();
        if (null == redisUtil.get("allowDay4AllPrice")) {
            allowDay.add("2021-09-16");
            allowDay.add("2021-09-17");
            redisUtil.set("allowDay4AllPrice", allowDay);
        }

        if (null == redisUtil.get("limitRecycler4Price")) {
            recyclers.add("18601780883");
            recyclers.add("18601780883");
            redisUtil.set("limitRecycler4Price", recyclers);
        }
        log.info("启动参数新增完成");
    }
}
