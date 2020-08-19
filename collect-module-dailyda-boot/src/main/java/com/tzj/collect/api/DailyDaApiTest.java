package com.tzj.collect.api;

import com.alibaba.fastjson.JSONArray;
import com.tzj.collect.DailyApplication;
import com.tzj.collect.core.service.DailyLexiconService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.tzj.collect.service.impl.DailyLexiconServiceImpl.redisKeyName;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={DailyApplication.class})
public class DailyDaApiTest {

    @Autowired
    private DailyLexiconService dailyLexiconService;
    @Resource
    private JedisPool jedisPool;

    @org.junit.Test
    public void dailyPersonRanking() {
        System.out.println("aaasasas"+ JSONArray.toJSONString(dailyLexiconService.getLastWeekMemberRank(50)));
    }

    /**
     * 手动给用户加分
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
//        aliUserIds.add("2088402394091585");
//        System.out.println("一等奖"+ JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds, "8.8")));
//       //二等奖
//        List<String> aliUserIds2 = new ArrayList<>();
//        aliUserIds2.add("2088712371983593");
//        aliUserIds2.add("2088422335256310");
//        System.out.println("二等奖"+ JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds2, "5.8")));
//      // 三等奖
//        List<String> aliUserIds3 = new ArrayList<>();
//        aliUserIds3.add("2088502945340912");
//        aliUserIds3.add("2088102079725652");
//        aliUserIds3.add("2088032282806674");
//        System.out.println("三等奖"+ JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds3, "1.8")));
//      // 四等奖
//        List<String> aliUserIds4 = new ArrayList<>();
//        aliUserIds4.add("2088532419153773");
//        aliUserIds4.add("2088032282806674");
//        aliUserIds4.add("2088432213879919");
//        aliUserIds4.add("2088622659660260");
//        aliUserIds4.add("2088502904090076");
//        aliUserIds4.add("2088622956590002");
//        aliUserIds4.add("2088012408921698");
//        aliUserIds4.add("2088812501151732");
//        aliUserIds4.add("2088432541945544");
//
//
//        System.out.println("四等奖"+ JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds4, "0.88")));
//    }
}