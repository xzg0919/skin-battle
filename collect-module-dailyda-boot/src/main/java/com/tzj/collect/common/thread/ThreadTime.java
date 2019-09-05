package com.tzj.collect.common.thread;

import com.tzj.collect.service.DailyMemberService;
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
//
//    @Resource
//    private DailyMemberService dailyMemberService;


    /**
     * 定时任务:每周十点执行（上周达人榜）
     */
    @Scheduled(cron = "0 0 10 ? * MON")
    public void startWeeklyRanking(){
        System.out.println("-----------------------分割线--------------------");
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new WeekRankingThread(dailyWeekRankingService)));
    }

//    /** 每周（三次）执行定时任务：发送模板消息给收呗用户（formId存在的用户）
//      * @author sgmark@aliyun.com
//      * @date 2019/9/4 0004
//      * @param
//      * @return
//      */
//    @Scheduled(cron = "0 0 8 0 0 1,3,5 *")
//    public void threeTimesSendMsgToAllMember(){
//        System.out.println("-----------------------分割线--------------------");
//        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new SendMsgToAllMemberThread(dailyMemberService)));
//    }
//
//    private class SendMsgToAllMemberThread implements Runnable {
//        private DailyMemberService dailyMemberService;
//        public SendMsgToAllMemberThread(DailyMemberService dailyMemberService) {
//            this.dailyMemberService = dailyMemberService;
//        }
//        @Override
//        public void run() {
//            dailyMemberService.sendMsgToAllMemberThread();
//        }
//    }
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
