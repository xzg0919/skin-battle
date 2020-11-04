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
@SpringBootTest(classes = {DailyApplication.class})
public class DailyDaApiTest {

    @Autowired
    private DailyLexiconService dailyLexiconService;

    @org.junit.Test
    public void dailyPersonRanking() {
        System.out.println("aaasasas" + JSONArray.toJSONString(dailyLexiconService.getLastWeekMemberRank(50)));
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
        List<String> aliUserIds = new ArrayList<>();
        aliUserIds.add("2088222297136967");
        aliUserIds.add("2088432267223513");
        System.out.println("一等奖" + JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds, "8.8")));
        //二等奖
        List<String> aliUserIds2 = new ArrayList<>();
        aliUserIds2.add("2088702041862097");
        aliUserIds2.add("2088432213879919");
        System.out.println("二等奖" + JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds2, "5.8")));
        // 三等奖
        List<String> aliUserIds3 = new ArrayList<>();
        aliUserIds3.add("2088702399722012");
        aliUserIds3.add("2088622677436377");
        aliUserIds3.add("2088002522355950");
        aliUserIds3.add("2088422335256310");
        aliUserIds3.add("2088802946936348");
        System.out.println("三等奖" + JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds3, "1.8")));
        // 四等奖
        List<String> aliUserIds4 = new ArrayList<>();

        aliUserIds4.add("2088622655761811");
        aliUserIds4.add("2088422526091359");
        aliUserIds4.add("2088622656261985");
        aliUserIds4.add("2088622672683532");
        aliUserIds4.add("2088132728718942");
        aliUserIds4.add("2088822773037269");
        aliUserIds4.add("2088112136567330");
        aliUserIds4.add("2088302586008785");
        aliUserIds4.add("2088432268711927");
        aliUserIds4.add("2088702041862097");
        aliUserIds4.add("2088102079725652");
        aliUserIds4.add("2088402394091585");
        aliUserIds4.add("2088112149593388");
        aliUserIds4.add("2088402737112432");
        aliUserIds4.add("2088012119272151");
        aliUserIds4.add("2088232060597710");
        aliUserIds4.add("2088712371983593");
        aliUserIds4.add("2088102079725652");
        aliUserIds4.add("2088622654516045");
        aliUserIds4.add("2088022624246064");
        aliUserIds4.add("2088622956590002");
        System.out.println("四等奖" + JSONArray.toJSONString(dailyLexiconService.sendMoneyByLocal(aliUserIds4, "0.88")));
    }
}