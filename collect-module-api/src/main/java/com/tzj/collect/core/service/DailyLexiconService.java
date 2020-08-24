package com.tzj.collect.core.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.daily.DailyDaParam;
import com.tzj.collect.entity.DailyLexicon;
import com.tzj.collect.entity.Member;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author sgmark@aliyun.com
 **/
public interface DailyLexiconService extends IService<DailyLexicon> {

     /** 随机抽选10道题
      * @author sgmark@aliyun.com
      * @date 2019/8/9 0009
      * @param
      * @return
      */
     Set<Map<String, Object>> dailyLexiconList(Member member);

     /** 所有的题库(id, name, type, depth)
      * 返回结构 [{depth =1, depthList=[{id =1, name= "",depth = 1}]},{depth =2, depthList=[{id =5, name= "",depth = 2}]}]
      * @author sgmark@aliyun.com
      * @date 2019/8/9 0009
      * @param lexiconList（固定参数值）
      * @return
      */
     @DS("slave")
     List<Map<String, Object>> lexiconList(String lexiconList);

     Map<String, Object> lexiconChecking(DailyDaParam dailyDaParam);

     Map<String, Object> memberInfo(Member member);

     String weekRankingByTime(Jedis jedis, Member member, String redisKeyName);

     Map<String, Object> weekRecords(Member member);

     List<Map<String, Object>> weekDresserList(Integer startPage, Integer pageSize);

     List<Map<String, Object>> weekRankingTop50(Jedis jedis, String redisKeyName);

     /**  上周前50达人记录
       * @author sgmark@aliyun.com
       * @date 2019/9/2 0002
       * @param
       * @return
       */
     @DS("slave")
     List<Map<String, Object>> weekDresserList();

     Map<String, Object> errorLexiconList(Member member);

     Set<Map<String, Object>> isAnswerDaily(String aliUserId);

     /**
      * 使用复活卡
      * @author: sgmark@aliyun.com
      * @Date: 2019/12/16 0016
      * @Param: 
      * @return: 
      */
     Map<String, Object> reloadCard(DailyDaParam dailyDaParam);

     List<Map<String, Object>> getLastWeekMemberRank(Integer number);

     String sendMoneyByLocal(List<String> aliUserIds, String finalPrice);

     Map<String, Object> getYesterdayNumber(String theDate);
}
