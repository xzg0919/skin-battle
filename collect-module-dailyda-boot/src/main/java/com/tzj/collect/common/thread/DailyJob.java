package com.tzj.collect.common.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 答答答定时任务
 *
 * @author sgmark
 * @create 2019-09-05 13:44
 **/

@Slf4j
@Component
public class DailyJob {

//    @Resource
//    private DailyMemberService dailyMemberService;
//
//    @Resource
//    private DailyWeekRankingService dailyWeekRankingService;



//    /** 每周（三次）执行定时任务：发送模板消息给收呗用户（formId存在的用户）
//     * @author sgmark@aliyun.com
//     * @date 2019/9/4 0004
//     * @param
//     * @return
//     */
//    @Scheduled(cron = "0 0 7 ? * 1,3,5")
////    @Scheduled(cron = "20 17 14 * * ?")
//    public void threeTimesSendMsgToAllMember(){
//        System.out.println("-----------------------分割线--------------------");
//        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new SendMsgToAllMemberThread(dailyMemberService)));
//    }
//
//    /**
//     * 定时任务:每周一 一点执行（上周达人榜）
//     */
//    @Scheduled(cron = "0 15 9 ? * MON")
//    public void startWeeklyRanking(){
//        System.out.println("-----------------------分割线--------------------");
//        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new WeekRankingThread(dailyWeekRankingService)));
//    }

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
//    /** 每周一 零点执行上周达人排行线程
//     * @author sgmark@aliyun.com
//     * @date 2019/8/19 0019
//     * @param
//     * @return
//     */
//    private class WeekRankingThread implements Runnable{
//        private DailyWeekRankingService dailyWeekRankingService;
//        public WeekRankingThread(DailyWeekRankingService dailyWeekRankingService){
//            this.dailyWeekRankingService = dailyWeekRankingService;
//        }
//        @Override
//        public void run() {
//            dailyWeekRankingService.insertEachWeekDresser();
//        }
//    }
}

