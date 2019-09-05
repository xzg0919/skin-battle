package com.tzj.collect.module.task.job;

import com.tzj.collect.module.task.job.thread.NewThreadPoorExcutor;
import com.tzj.collect.service.DailyMemberService;
import com.tzj.collect.service.DailyWeekRankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 每周一三五消息推送线程
 *
 * @author sgmark
 * @create 2019-09-05 13:44
 **/

@Slf4j
@Component
public class DailyJob {

    @Resource
    private DailyMemberService dailyMemberService;




    /** 每周（三次）执行定时任务：发送模板消息给收呗用户（formId存在的用户）
     * @author sgmark@aliyun.com
     * @date 2019/9/4 0004
     * @param
     * @return
     */
    @Scheduled(cron = "* * 8 * * 1,3,5")
//    @Scheduled(cron = "20 17 14 * * ?")
    public void threeTimesSendMsgToAllMember(){
        System.out.println("-----------------------分割线--------------------");
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new SendMsgToAllMemberThread(dailyMemberService)));
    }

    private class SendMsgToAllMemberThread implements Runnable {
        private DailyMemberService dailyMemberService;
        public SendMsgToAllMemberThread(DailyMemberService dailyMemberService) {
            this.dailyMemberService = dailyMemberService;
        }
        @Override
        public void run() {
            dailyMemberService.sendMsgToAllMemberThread();
        }
    }
}
