package com.tzj.collect.core.thread;

import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.core.service.PaymentService;
import com.tzj.collect.core.service.VoucherAliService;
import com.tzj.collect.core.service.VoucherMemberService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.entity.VoucherMember;
import groovy.util.logging.Slf4j;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ThreadTime {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private VoucherMemberService voucherMemberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private VoucherAliService voucherAliService;

//    @Resource
//    private DailyWeekRankingService dailyWeekRankingService;
    /**
     * 定时任务。定时执行回收人员支付完成，单钱未转账到用户支付宝
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void startPaymentExecute() {
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread(new PaymentThread(paymentService, voucherMemberService, orderService,voucherAliService)));
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

class PaymentThread implements Runnable {

    private VoucherAliService voucherAliService;
    private PaymentService paymentService;
    private VoucherMemberService voucherMemberService;
    private OrderService orderService;

    public PaymentThread(PaymentService paymentService, VoucherMemberService voucherMemberService, OrderService orderService,VoucherAliService voucherAliService) {
        this.paymentService = paymentService;
        this.voucherMemberService = voucherMemberService;
        this.orderService = orderService;
        this.voucherAliService = voucherAliService;
    }

    @Override
    public void run() {
        System.out.println("执行定时器---------------------------");
        //查询交易是否有未完成的
        List<Payment> paymentsList = paymentService.selectList(new EntityWrapper<Payment>().eq("del_flag", 0).isNotNull("ali_user_id").isNotNull("trade_no").eq("status_", 1).orderBy("id",false));
        if (!paymentsList.isEmpty()) {
            for (Payment payment : paymentsList) {
                //查询此单交易是否成功
                AlipayFundTransOrderQueryResponse aliPayment = paymentService.getTransfer(payment.getId().toString());
                if ("Success".equals(aliPayment.getMsg()) && "SUCCESS".equals(aliPayment.getStatus())) {
                    //转账成功
                    System.out.println("查询转账接口成功，更正转账信息");
                    payment.setStatus(Payment.STATUS_TRANSFER);
                    paymentService.updateById(payment);
                } else {
                    //根據order_no查询相关订单
                    Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", payment.getOrderSn()).eq("del_flag", 0));
                    //根据订单号查询绑定券的信息
                    VoucherMember voucherMember = voucherMemberService.selectOne(new EntityWrapper<VoucherMember>().eq("order_no", payment.getOrderSn()).eq("ali_user_id", payment.getAliUserId()));
                    //交易失败，重新转账
                    if ((Order.TitleType.BIGTHING + "").equals(order.getTitle() + "")) {
                        payment.setDiscountPrice(order.getAchPrice());
                        payment.setTransferPrice(order.getAchPrice().subtract(order.getCommissionsPrice()));
                    } else {
                        if(null != voucherMember){
                            //如果不是大件并且有优惠券，则计算使用该券后的价格给用户进行转账
                            BigDecimal discountPrice = voucherAliService.getDiscountPriceByVoucherId(order.getAchPrice(), voucherMember.getId().toString());
                            payment.setDiscountPrice(discountPrice);
                            payment.setTransferPrice(discountPrice);
                        }else {
                            payment.setDiscountPrice(order.getAchPrice());
                            payment.setTransferPrice(order.getAchPrice());
                        }
                    }
                    payment.setVoucherMember(voucherMember);
                    paymentService.transfer(payment);
                }
            }
        }
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
