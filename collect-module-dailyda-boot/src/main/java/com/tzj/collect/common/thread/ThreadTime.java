package com.tzj.collect.common.thread;

import com.tzj.collect.service.DailyWeekRankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ThreadTime {

    @Resource
    private DailyWeekRankingService dailyWeekRankingService;


    /**
     * 定时任务:每周十点执行（上周达人榜）
     */
    @Scheduled(cron = "0 0 10 ? * MON")
    public void startWeeklyRanking(){
        System.out.println("-----------------------分割线--------------------");
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new WeekRankingThread(dailyWeekRankingService)));
    }
}


/** 每周一 十点执行上周达人排行线程
  * @author sgmark@aliyun.com
  * @date 2019/8/19 0019
  * @param
  * @return
  */
class WeekRankingThread implements Runnable{
    private DailyWeekRankingService dailyWeekRankingService;
    public WeekRankingThread(DailyWeekRankingService dailyWeekRankingService){
        this.dailyWeekRankingService = dailyWeekRankingService;
    }
    @Override
    public void run() {
        dailyWeekRankingService.insertEachWeekDresser();
    }
}