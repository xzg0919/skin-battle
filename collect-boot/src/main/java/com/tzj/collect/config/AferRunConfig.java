package com.tzj.collect.config;

import com.tzj.collect.commom.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Map<String,String>> limitCompleteTimeMap = new ArrayList<>();
        List<String> limitCompleteRecyclers = new ArrayList<>();
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

        if (null == redisUtil.get("limitCompleteTime")) {
            Map<String,String> limitTime1 =new HashMap<>();
            Map<String,String> limitTime2 =new HashMap<>();
            limitTime1.put("beforeTime","20:00:00");
            limitTime1.put("afterTime","24:00:00");
            limitTime2.put("beforeTime","00:00:00");
            limitTime2.put("afterTime","07:00:00");
            limitCompleteTimeMap.add(limitTime1);
            limitCompleteTimeMap.add(limitTime2);
            redisUtil.set("limitCompleteTime", limitCompleteTimeMap);
        }

        if (null == redisUtil.get("limitCompleteRecyclers")) {
            limitCompleteRecyclers.add("13016666600");
            redisUtil.set("limitCompleteRecyclers", limitCompleteRecyclers);
        }

        //用户限制每日完成订单数量
         redisUtil.set("userLimitCompleteCount", 3);

        log.info("启动参数新增完成");



    }
}
