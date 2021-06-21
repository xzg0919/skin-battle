package com.tzj.collect.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.DailyApplication;
import com.tzj.collect.core.service.DailyLexiconService;
import com.tzj.collect.core.service.PaymentService;
import com.tzj.collect.entity.Payment;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.tzj.collect.service.impl.DailyLexiconServiceImpl.redisKeyName;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DailyApplication.class})
@WebAppConfiguration
public class DailyDaApiTest {

    @Autowired
    private DailyLexiconService dailyLexiconService;
//    @Autowired
//    private PaymentService paymentService;

//    @org.junit.Test
//    public void dailyPersonRanking() {
//        Payment payment = new Payment();
//        payment.setAliUserId("1212121");
//        paymentService.insertOrUpdate(payment);
//        System.out.println(JSONObject.toJSONString(payment));
//        paymentService.transferDemo(payment);
////        System.out.println("aaasasas" + JSONArray.toJSONString(dailyLexiconService.getLastWeekMemberRank(50)));
//    }

    /**
     * 手动给用户加分20882316766360532088231676636053
     */
//    @org.junit.Test
//    public void addHandeGradeByUserId() {
//        Jedis jedis = jedisPool.getResource();
//        //正确答题积分加十(周记录)
//        jedis.zincrby(redisKeyName(), 10, "2088702041862097");
//        //每日答题数据记录
//        jedis.zincrby(redisKeyName() +":"+ LocalDate.now().getDayOfWeek(), 10, "2088702041862097");
//    }
    @org.junit.Test
    public void sendMoneyByLocal() {
        //一等奖
//        List<String> aliUserIds = new ArrayList<>();
//        aliUserIds.add("2088132728718942");
//        System.out.println("一等奖" + JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds, "8.8")));
        //二等奖
//        List<String> aliUserIds2 = new ArrayList<>();
//        aliUserIds2.add("2088122054532143");
//        aliUserIds2.add("2088622677436377");
//        System.out.println("二等奖" + JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds2, "5.8")));
//        // 三等奖
//        List<String> aliUserIds3 = new ArrayList<>();
//        aliUserIds3.add("2088432817709520");
//        aliUserIds3.add("2088002522355950");
//        aliUserIds3.add("2088222151985582");
//        System.out.println("三等奖" + JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds3, "1.8")));
        // 四等奖
        List<String> aliUserIds4 = new ArrayList<>();
        aliUserIds4.add("2088002652357066");
        System.out.println("四等奖" + JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds4, "0.88")));
    }
}