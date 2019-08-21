package com.tzj.collect.service.impl;


import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.lexicon.param.DailyDaParam;
import com.tzj.collect.common.utils.MemberUtils;
import com.tzj.collect.core.mapper.DailyReceivingMapper;
import com.tzj.collect.core.service.PaymentService;
import com.tzj.collect.entity.DailyReceiving;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.service.DailyReceivingService;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tzj.collect.entity.Payment.STATUS_PAYED;
import static com.tzj.collect.entity.Payment.STATUS_TRANSFER;

/**
 * @author sgmark
 * @create 2019-08-09 16:08
 **/
@Service
@Transactional(readOnly = true)
public class DailyReceivingServiceImpl extends ServiceImpl<DailyReceivingMapper, DailyReceiving> implements DailyReceivingService {

    @Resource
    private DailyReceivingMapper dailyReceivingMapper;

    @Resource
    private PaymentService paymentService;

    /** 用户领取记录
      * @author sgmark@aliyun.com
      * @date 2019/8/15 0015
      * @param
      * @return
      */
    @Override
    public Map<String, Object> memberReceivingRecords(String aliUserId) {
        Map<String, Object> returnMap = new HashMap<>();
        Integer week = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
        //用户签到天数(有答题记录的天数)
        Integer signNum = dailyReceivingMapper.signNum(aliUserId, DailyLexiconServiceImpl.tableName(System.currentTimeMillis()));
        returnMap.put("signNum", signNum);
        List<Map<String, Object>> receiveNumList = null;
        try {
            receiveNumList = dailyReceivingMapper.receiveNum(aliUserId, LocalDate.now().getYear() + "" + week);
        }catch (Exception e){
            receiveNumList = new ArrayList<>();
        }
        //领奖天数
        returnMap.put("receiveNum", receiveNumList.size());
        List<Map<String, Object>> returnListMap = new ArrayList<>(3);
        Map<String, Object> bagMap;
        AtomicInteger setNum = new AtomicInteger(1);
        while (returnListMap.size() < 4){
            bagMap = new HashMap<>();
            bagMap.put("setNum", setNum.getAndIncrement());
            bagMap.put("isReceive", "N");
            bagMap.put("isReceivable", "N");
            returnListMap.add(bagMap);
        }
        //设值是否可领取
        AtomicInteger signNumInt = new AtomicInteger(1);
        while (signNumInt.get() <=   signNum){
            Map<String, Object> map = returnListMap.get((signNumInt.get() - 1) / 2);
            returnListMap.get((signNumInt.get() - 1) / 2).put("isReceivable", "Y");
            signNumInt.incrementAndGet();
        }

        //设值是否已领取
        List<Map<String, Object>> finalReceiveNumList = receiveNumList;
        returnListMap.stream().forEach(returnListMaps ->
            finalReceiveNumList.stream().forEach(receiveNumLists ->{
                if (returnListMaps.get("setNum").equals(receiveNumLists.get("set_num"))){
                    if ("0".equals(receiveNumLists.get("is_receive").toString())){
                        returnListMaps.put("isReceive", "Y");
                        returnListMaps.put("isReceivable", "N");
                    }
                }
            })
        );

        returnMap.put("bagList", returnListMap);
        return returnMap;
    }

    @Override
    @Transactional(readOnly = false ,rollbackFor = Exception.class)
    public Map<String, Object> receivingMoney(DailyDaParam dailyDaParam) {
        Integer week = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
        //领红包时创建红包(aliUserId, week, setNum)
//        String price = (Math.random() * 0.4+0.1+"").substring(0,4);
        String price = "0.11";
        while(Double.parseDouble(price) >= 0.50) {
            price = (Math.random() * 0.4+0.1+"").substring(0,4);
        }
        DailyReceiving dailyReceiving = this.selectOne(new EntityWrapper<DailyReceiving>().eq("del_flag", 0).eq("ali_user_id", dailyDaParam.getAliUserId()).eq("week_", LocalDate.now().getYear() + "" + week).eq("set_num", dailyDaParam.getSetNum()));
        if (null == dailyReceiving){
            dailyReceiving = new DailyReceiving();
            dailyReceiving.setPrice(Double.parseDouble(price));
            dailyReceiving.setAliUserId(dailyDaParam.getAliUserId());
            dailyReceiving.setWeek(LocalDate.now().getYear()+""+week);
            dailyReceiving.setUuId(UUID.randomUUID().toString());
            dailyReceiving.setSetNum(dailyDaParam.getSetNum());
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("msg", "领取失败");
        returnMap.put("price", 0);
        String aliUserId = MemberUtils.getMember().getAliUserId();
        Map<String, Object> receivableMap = this.memberReceivingRecords(aliUserId);
        List<Map<String, Object>> receivableList = (List<Map<String, Object>>) receivableMap.get("bagList");
        String finalPrice = price;
        DailyReceiving finalDailyReceiving = dailyReceiving;
        receivableList.stream().forEach(receivableLists ->{
            //用户传过来的红包位置和现有的红包位置一致
            if(receivableLists.get("setNum").equals(dailyDaParam.getSetNum())){
                //红包未领取并且处于可领取状态
                if ("N".equals(receivableLists.get("isReceive")) && "Y".equals(receivableLists.get("isReceivable"))){
                    //创建红包时产生的uuId
                    String outBizNo = finalDailyReceiving.getUuId();
                    //更新成功, 调用转账接口
                    //记录转账
                    Payment payment = paymentService.selectByOrderSn(outBizNo);
                    if (null == payment){
                        payment = new Payment();
                    }
                    //用户转账
                    AlipayFundTransToaccountTransferResponse alipayFundTransToaccountTransferResponse = paymentService.dailyDaTransfer(aliUserId, finalPrice, outBizNo);
                    payment.setAliUserId(aliUserId);
                    payment.setPrice(new BigDecimal(finalPrice));
                    payment.setOrderSn(outBizNo);
                    payment.setSellerId(aliUserId);
                    payment.setPayType(Payment.PayType.RED_BAG);
                    if("Success".equals(alipayFundTransToaccountTransferResponse.getMsg())){
                        //交易完成(状态设置为已转账)
                        payment.setStatus(STATUS_TRANSFER);
                        payment.setIsSuccess("1");
                        payment.setTradeNo(alipayFundTransToaccountTransferResponse.getOrderId());
                        finalDailyReceiving.setIsReceive(0);
                    }else {
                        //交易失败(状态设置为未转账)
                        payment.setStatus(STATUS_PAYED);
                        payment.setIsSuccess("0");
                        finalDailyReceiving.setIsReceive(1);
                    }
                    paymentService.insertOrUpdate(payment);

                    this.insertOrUpdate(finalDailyReceiving);
                    returnMap.put("msg", "领取成功");
                    returnMap.put("price", finalPrice);
                    return;
                }else if("Y".equals(receivableLists.get("isReceive"))){
                    throw new ApiException("红包已领取");
                }else if ("N".equals(receivableLists.get("isReceivable"))){
                    throw new ApiException("红包处于不可领取状态下");
                }
            }
        });
        return returnMap;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++){
            System.out.println((Math.random() * 0.4+0.1+"").substring(0,4));
        }
    }
}

