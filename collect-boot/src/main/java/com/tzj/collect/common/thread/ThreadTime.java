package com.tzj.collect.common.thread;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.common.constant.MD5Util;
import com.tzj.collect.core.param.ali.MemberAddressBean;
import com.tzj.collect.core.service.*;
//import com.tzj.collect.entity.DailyWeekRanking;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderItemAch;
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
    @Autowired
    private OrderService orderService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private OrderItemAchService orderItemAchService;
//    @Resource
//    private DailyWeekRankingService dailyWeekRankingService;
    /**
     * 定时任务。定时执行回收人员支付完成，单钱未转账到用户支付宝
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void startPaymentExecute(){
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new PaymentThread(paymentService)));
    }
//    @Scheduled(cron = "30 27 17 ? * MON")
//    public void reSendExecute(){
//        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new ReSendExecute(orderService, areaService, orderItemAchService)));
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
                if("Success".equals(aliPayment.getMsg())&&"SUCCESS".equals(aliPayment.getStatus())){
                    //转账成功
                    payment.setIsSuccess("1");
                    paymentService.updateById(payment);
                }else{
                    //交易失败，重新转账
                    System.out.println("交易失败，重新转账");
                    paymentService.transfer(payment);
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

class ReSendExecute implements Runnable{

    private OrderService orderService;
    private AreaService areaService;
    private OrderItemAchService orderItemAchService;

    public ReSendExecute(OrderService orderService,AreaService areaService,OrderItemAchService orderItemAchService){
        this.orderService = orderService;
        this.areaService = areaService;
        this.orderItemAchService = orderItemAchService;
    }
    @Override
    public void run() {
        Page<Order> orderPage = orderService.selectPage(new Page<>(1, 10000), new EntityWrapper<Order>().eq("del_flag", 0).eq("status_", 3).eq("title", 2).between("create_date", "2019-10-08", "2019-10-25 23:59:59").like("address", "上海市"));
        final List<Order> orders = orderPage.getRecords();
        orders.stream().forEach(order ->{
            Map<String,Object> body = new HashMap<>();
            List<Map<String,Object>> recycleOrderList = new ArrayList<>();
            Map<String,Object> resultMap = new HashMap<>();
            Map<String,Object> head = new HashMap<>();

            Area street = areaService.selectById(order.getStreetId());
            Area area = areaService.selectById(order.getAreaId());
            List<OrderItemAch> orderItemAches = orderItemAchService.selectByOrderId(order.getId().intValue());
            double totalWeight = orderItemAches.stream().mapToDouble(orderItemAch -> orderItemAch.getAmount()).sum();

            orderItemAches.stream().forEach(orderItemAch ->{
                Map<String,Object> recycleOrder = new HashMap<>();
                if("五废".equals(orderItemAch.getParentName())){
                    recycleOrder.put("resourceNo",sendGreenOrderThread.getResourceNo(orderItemAch.getCategoryName()));
                    recycleOrder.put("resourceName",orderItemAch.getCategoryName());
                }else {
                    recycleOrder.put("resourceNo",sendGreenOrderThread.getResourceNo(orderItemAch.getParentName()));
                    recycleOrder.put("resourceName",orderItemAch.getParentName());
                }
                recycleOrder.put("resourceUnit","kg");
                recycleOrder.put("resourceWeight",orderItemAch.getAmount());
                recycleOrderList.add(recycleOrder);
            });

            //String jiuyuUrl="https://recycletest.greenfortune.sh.cn/reclaim/api/twoNet/receiveRecycleOrderData";
            String jiuyuUrl="https://recycle.greenfortune.sh.cn/reclaimi/api/twoNet/receiveRecycleOrderData";

            String reqCode = "30004";
            head.put("reqTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            head.put("version","1.0");
            head.put("reqCode",reqCode);
            body.put("dataCollectionProvider","125");
            body.put("orderNo",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000));
            body.put("orderType","生活垃圾");
            body.put("orderStatus","完成");
            body.put("orderCreateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreateDate()));
            body.put("orderFinishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            body.put("orderReceiveTime",order.getReceiveTime());
            body.put("orderContact",order.getLinkMan());
            body.put("orderPhoneNum",order.getTel());
            body.put("detailAddress",order.getAddress());
            body.put("orderCreateUserPhone",order.getTel());
            body.put("orderReceiveName","上海铸乾信息科技有限公司");
            body.put("companyName","上海铸乾信息科技有限公司");
            body.put("orderReceivePhone","02180394620");
            body.put("communityCode",street.getCode());
            body.put("communityName",street.getAreaName());
            body.put("committeeCode",street.getCode());
            body.put("committeeName",street.getAreaName());
            body.put("streetCode",street.getCode());
            body.put("streetName",street.getAreaName());
            body.put("areaCode",area.getCode()+"000000");
            body.put("areaName",area.getAreaName());
            body.put("totalWeight",totalWeight);
            body.put("orderActualPoints",order.getGreenCount());
            body.put("recycleOrderList",recycleOrderList);
            resultMap.put("head",head);
            resultMap.put("body",body);
            resultMap.put("sign", MD5Util.encode(JSON.toJSONString(body)+reqCode+"eefbf3cca60830313a95283c368687e3"));
            System.out.println("请求的参数是 ："+JSON.toJSONString(resultMap));
            Response response= null;
            try {
                response = FastHttpClient.post().url(jiuyuUrl).body(JSON.toJSONString(resultMap)).build().execute();
                String resultJson=response.body().string();
                System.out.println("返回的参数是 ："+resultJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}