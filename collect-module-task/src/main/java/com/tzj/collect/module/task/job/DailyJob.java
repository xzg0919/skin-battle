package com.tzj.collect.module.task.job;

import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.module.task.job.thread.NewThreadPoorExcutor;
import com.tzj.collect.service.DailyMemberService;
import com.tzj.collect.service.DailyPaymentService;
import com.tzj.collect.service.DailyWeekRankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 答答答定时任务
 *
 * @author sgmark
 * @create 2019-09-05 13:44
 **/

@Slf4j
@Component
public class DailyJob {

    @Resource
    private DailyMemberService dailyMemberService;

    @Resource
    private DailyWeekRankingService dailyWeekRankingService;

    @Resource
    private DailyPaymentService dailyPaymentService;

    /**
     * 定时任务,钱未转账到用户支付宝
     */
    @Scheduled(cron = "30 29 20 ? * 1,3,5")
    public void startPaymentExecute(){
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new DailyPaymentThread(dailyPaymentService)));
    }

    /** 每周（1次）执行定时任务：上传上周排行榜记录到oss(供用户下载)
     * @author sgmark@aliyun.com
     * @date 2019/9/4 0004
     * @param
     * @return
     */
    @Scheduled(cron = "0 0 2 ? * 1")
    public void downloadExcel(){
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new UploadExcelThread(dailyWeekRankingService)));
    }

    /** 每周（三次）执行定时任务：发送模板消息给收呗用户（formId存在的用户）
     * @author sgmark@aliyun.com
     * @date 2019/9/4 0004
     * @param
     * @return
     */
    @Scheduled(cron = "0 0 7 ? * 1,3,5")
//    @Scheduled(cron = "20 17 14 * * ?")
    public void threeTimesSendMsgToAllMember(){
        System.out.println("-----------------------分割线--------------------");
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new SendMsgToAllMemberThread(dailyMemberService)));
    }

    /**
     * 定时任务:每周一 一点执行（上周达人榜）
     */
    @Scheduled(cron = "0 0 1 ? * MON")
    public void startWeeklyRanking(){
        System.out.println("-----------------------分割线--------------------");
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new WeekRankingThread(dailyWeekRankingService)));
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
    /** 每周一 零点执行上周达人排行线程
     * @author sgmark@aliyun.com
     * @date 2019/8/19 0019
     * @param
     * @return
     */
    private class WeekRankingThread implements Runnable{
        private DailyWeekRankingService dailyWeekRankingService;
        public WeekRankingThread(DailyWeekRankingService dailyWeekRankingService){
            this.dailyWeekRankingService = dailyWeekRankingService;
        }
        @Override
        public void run() {
            dailyWeekRankingService.insertEachWeekDresser();
        }
    }

    /**
     * 定时任务,钱未转账到用户支付宝
     */
    private class DailyPaymentThread implements Runnable{
        private DailyPaymentService paymentService;
        public DailyPaymentThread(DailyPaymentService paymentService){
            this.paymentService = paymentService;
        }
        @Override
        public void run() {
            //查询交易是否有未完成的
            List<Payment> paymentsList = paymentService.selectList(new EntityWrapper<Payment>().eq("is_success","0").eq("del_flag", 0).isNotNull("ali_user_id").eq("status_",1).eq("pay_type", 1));
            if (!CollectionUtils.isEmpty(paymentsList)){
                paymentsList.parallelStream().forEach(paymentsLists -> {
                    //查询此单交易是否成功(根据订单号查询订单是否转账成功)
                    AlipayFundTransOrderQueryResponse aliPayment = paymentService.getTransfer(paymentsLists.getOrderSn());
                    if(!"Success".equals(aliPayment.getMsg())){
                        //交易失败，重新转账
                        System.out.println("交易失败，重新转账");
                        AlipayFundTransToaccountTransferResponse alipayFundTransToaccountTransferResponse = paymentService.dailyDaTransfer(paymentsLists.getAliUserId(), paymentsLists.getPrice().toString(),paymentsLists.getOrderSn());
//                        成功更新状态
                        if ("Success".equals(alipayFundTransToaccountTransferResponse.getMsg())){
                            paymentsLists.setStatus(2);
                            //转账成功
                            paymentsLists.setIsSuccess("1");
                            paymentsLists.setUpdateDate(new Date());
                            paymentsLists.setTradeNo(alipayFundTransToaccountTransferResponse.getOrderId());
                            paymentService.updateById(paymentsLists);
                        }else {
                            paymentsLists.setIsSuccess("1");
                            paymentsLists.setUpdateDate(new Date());
                            paymentsLists.setRemarks(alipayFundTransToaccountTransferResponse.getSubMsg());
                            paymentService.updateById(paymentsLists);
                        }
                    }else{
                        paymentsLists.setUpdateDate(new Date());
                        paymentsLists.setStatus(2);
                        //转账成功
                        paymentsLists.setIsSuccess("1");
                        paymentsLists.setTradeNo(aliPayment.getOrderId());
                        paymentService.updateById(paymentsLists);
                    }
                });
            }
        }
    }

    /** 每周（1次）执行定时任务：上传上周排行榜记录到oss(供用户下载)
     * @author sgmark@aliyun.com
     * @date 2019/9/4 0004
     * @param
     * @return
     */
    private class UploadExcelThread implements Runnable {

        private DailyWeekRankingService dailyWeekRankingService;

        public UploadExcelThread(DailyWeekRankingService dailyWeekRankingService) {
            this.dailyWeekRankingService = dailyWeekRankingService;
        }

        @Override
        public void run() {
            dailyWeekRankingService.uploadExcel();
        }
    }
}

