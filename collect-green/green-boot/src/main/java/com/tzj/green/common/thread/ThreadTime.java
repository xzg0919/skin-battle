package com.tzj.green.common.thread;

import com.tzj.green.service.ProductService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ThreadTime {

    @Autowired
    private ProductService productService;

//    @Resource
//    private DailyWeekRankingService dailyWeekRankingService;
    /**
     * 定时任务。定时执行回收人员支付完成，单钱未转账到用户支付宝
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void startPaymentExecute() {
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread(new GoodsFrozen(productService)));
    }

//
//    /**
//     * 定时任务:每周十点执行（上周达人榜）
//     */
//    @Scheduled(cron = "0 0 10 ? * MON")
//    public void startWeeklyRanking(){
//        System.out.println("-----------------------分割线--------------------");
//        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new WeekRankingThread(dailyWeekRankingService)));
//    }
}

class GoodsFrozen implements Runnable {

    private ProductService productService;

    public GoodsFrozen(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run() {
        productService.updateGoodsFrozen();
    }
}
/**
 * 每周一 十点执行上周达人排行线程
 *
 * @author sgmark@aliyun.com
 * @date 2019/8/19 0019
 * @param
 * @return //
 */
//class WeekRankingThread implements Runnable{
//    private DailyWeekRankingService dailyWeekRankingService;
//    public WeekRankingThread(DailyWeekRankingService dailyWeekRankingService){
//        this.dailyWeekRankingService = dailyWeekRankingService;
//    }
//    @Override
//    public void run() {
//        dailyWeekRankingService.insertEachWeekDresser();
//    }
//}
