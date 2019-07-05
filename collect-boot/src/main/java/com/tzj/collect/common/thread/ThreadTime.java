package com.tzj.collect.common.thread;

import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.MemberAddressBean;
import com.tzj.collect.entity.MemberAddress;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.service.MemberAddressService;
import com.tzj.collect.service.PaymentService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ThreadTime {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private MemberAddressService memberAddressService;

    /**
     * 定时任务。定时执行回收人员支付完成，单钱未转账到用户支付宝
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void startPaymentExecute(){
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new PaymentThread(paymentService)));
    }

    /**
     * 定时任务。定时执行更新新的街道进入更新用户街道地址信息
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void startUpdateMemberAddressByStreetId(){
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new MemberAddressUpdateByStreetId(memberAddressService)));
    }

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
class MemberAddressUpdate implements Runnable{

    private MemberAddressService memberAddressService;

    public MemberAddressUpdate(MemberAddressService memberAddressService){
        this.memberAddressService = memberAddressService;
    }
    @Override
    public void run() {
        List<MemberAddressBean> memberAddressesList = memberAddressService.selectMemberAddressByCommunityId();
        if(null != memberAddressesList&&!memberAddressesList.isEmpty()){
            for (MemberAddressBean memberAddressBean:memberAddressesList){
                memberAddressService.updateMemberAddress(memberAddressBean.getId(),memberAddressBean.getCommunityId() );
            }
        }
    }
}

class MemberAddressUpdateByStreetId implements Runnable{

    private MemberAddressService memberAddressService;

    public MemberAddressUpdateByStreetId(MemberAddressService memberAddressService){
        this.memberAddressService = memberAddressService;
    }
    @Override
    public void run() {
        memberAddressService.MemberAddressUpdateStreetId();
    }
}