package com.tzj.collect.common.thread;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.ali.MemberAddressBean;
import com.tzj.collect.core.service.MemberAddressService;
import com.tzj.collect.core.service.PaymentService;
//import com.tzj.collect.entity.DailyWeekRanking;
import com.tzj.collect.entity.Payment;
//import com.tzj.collect.service.DailyWeekRankingService;
import groovy.util.logging.Slf4j;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class ThreadTime {
    @Autowired
    private PaymentService paymentService;

//    @Resource
//    private DailyWeekRankingService dailyWeekRankingService;
    /**
     * 定时任务。定时执行回收人员支付完成，单钱未转账到用户支付宝
     */
//    @Scheduled(cron = "0 0/2 * * * ?")
//    public void startPaymentExecute(){
//        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new PaymentThread(paymentService)));
//    }

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
class PaymentThread implements Runnable{
    private PaymentService paymentService;
    public PaymentThread(PaymentService paymentService){
        this.paymentService = paymentService;
    }
    @Override
    public void run() {
        //查询交易是否有未完成的
        List<Payment> paymentsList = paymentService.selectList(new EntityWrapper<Payment>().eq("is_success","0").eq("del_flag", 0).isNotNull("ali_user_id").isNotNull("trade_no").eq("status_",1));
        if (!paymentsList.isEmpty()){
            for(Payment payment:paymentsList){
                //查询此单交易是否成功
            	AlipayFundTransOrderQueryResponse aliPayment = paymentService.getTransfer(payment.getId().toString());
                if(!"Success".equals(aliPayment.getMsg())){
                	 //交易失败，重新转账
                    System.out.println("交易失败，重新转账");
                    paymentService.transfer(payment);
                }else{
                    //转账成功
                    payment.setIsSuccess("1");
                    paymentService.updateById(payment);
                }
            }
        }
    }
}
/** 每周一 十点执行上周达人排行线程
  * @author sgmark@aliyun.com
  * @date 2019/8/19 0019
  * @param
  * @return
//  */
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