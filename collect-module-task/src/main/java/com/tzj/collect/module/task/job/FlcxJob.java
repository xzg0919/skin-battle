package com.tzj.collect.module.task.job;

import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.module.task.job.thread.NewThreadPoorExcutor;
import com.tzj.collect.service.FlcxRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 分类查询定期获取查询记录
 *
 * @author sgmark
 * @create 2019-07-03 19:58
 **/
@Component
public class FlcxJob {

    @Resource
    private FlcxRecordsService flcxRecordsService;
    @Autowired
    private RedisUtil redisUtil;
    protected final static Logger logger = LoggerFactory.getLogger(FlcxJob.class);
    /**
     * 定时任务。（每天一点）
     */
    @Scheduled(cron = "0 40 0 * * ?")
    public void flcxExecute(){
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new flcxThread(flcxRecordsService, redisUtil)));
    }

    class flcxThread implements Runnable{
        private FlcxRecordsService flcxRecordsService;
        private RedisUtil redisUtil;
        public flcxThread(FlcxRecordsService flcxRecordsService, RedisUtil redisUtil){
            this.flcxRecordsService = flcxRecordsService;
            this.redisUtil = redisUtil;
        }
        @Override
        public void run() {
            //每天查询数据库记录，更新
            logger.info("我打算更新redis中词库使用次数--------------------------------");
            System.out.println("我打算更新redis中词库使用次数--------------------------------");
            List<Map<String, Object>> mapList = flcxRecordsService.selectRecordsCountList();
            //记录昨天查询总数放入redis
            mapList.stream().forEach(map -> {
                redisUtil.set(map.get("name_").toString(), map.get("count_"));
            });
        }
    }
}
