package com.tzj.collect.common.thread;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.constant.MD5;
import com.tzj.collect.core.param.ali.MemberAddressBean;
import com.tzj.collect.core.service.MemberAddressService;
import com.tzj.collect.core.service.PaymentService;
import com.tzj.collect.entity.Payment;
import groovy.util.logging.Slf4j;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

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
     * 定时任务。定时执行更新新的街道进入更新用户小区地址信息
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void startUpdateMemberAddressByCommunityId(){
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new MemberAddressUpdate(memberAddressService)));
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

class GetOrderList implements Runnable{

    private final static String jiuyuUrl="https://recycletest.greenfortune.sh.cn/reclaim/api/twoNet/receiveRecycleOrderData";

    //private final static String jiuyuUrl="http://10.100.9.61/reclaim/api/recyclePlatform/";

    private  static Map<String,Object> body = null;
    private  static Map<String,Object> recycleOrderList = null;
    private  static Map<String,Object> resultMap = null;
    private  static Map<String,Object> head = null;
    @Override
    public void run() {

    }

    public static void main(String[] args) throws Exception {
        head = new HashMap<>();
        body = new HashMap<>();
        recycleOrderList = new HashMap<>();
        resultMap = new HashMap<>();
        String reqCode = UUID.randomUUID().toString();
        head.put("reqTime",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        head.put("version","1.0");
        head.put("reqCode",reqCode);
        body.put("dataCollectionProvider","125");
        body.put("orderNo",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000));
        body.put("orderType","orderType");
        body.put("orderStatus","4");
        body.put("orderCreateTime",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        body.put("orderFinishTime",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        body.put("orderReceiveTime",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        body.put("orderContact","魏忠贤");
        body.put("orderPhoneNum","14141414441");
        body.put("detailAddress","我家就在黄土高坡");
        body.put("orderCreateUserPhone","14141414441");
        body.put("communityCode","110102019000");
        body.put("communityName","白纸坊街道");
        body.put("committeeCode","110102019000");
        body.put("committeeName","白纸坊街道");
        body.put("streetCode","110102019000");
        body.put("streetName","白纸坊街道");
        body.put("areaCode","110102");
        body.put("areaName","西城区");
        body.put("totalWeight","10");
        body.put("orderActualPoints","10");
        recycleOrderList.put("resourceUnit","kg");
        recycleOrderList.put("resourceNo","1");
        recycleOrderList.put("resourceName","废玻璃");
        recycleOrderList.put("resourceWeight","5");
        body.put("recycleOrderList",recycleOrderList);
        resultMap.put("head",head);
        resultMap.put("body",body);
        System.out.println(JSON.toJSONString(body)+reqCode+"eefbf3cca60830313a95283c368687e3");
        resultMap.put("sign", MD5.md5(JSON.toJSONString(body)+reqCode+"eefbf3cca60830313a95283c368687e3"));
        //PostTool.postB(jiuyuUrl,JSON.toJSONString(resultMap));
        System.out.println("请求的参数是 ："+JSON.toJSONString(resultMap));
        Response response= FastHttpClient.post().url(jiuyuUrl).body(JSON.toJSONString(resultMap)).build().execute();
        String resultJson=response.body().string();
        System.out.println("返回的参数是 ："+resultJson);
    }
}