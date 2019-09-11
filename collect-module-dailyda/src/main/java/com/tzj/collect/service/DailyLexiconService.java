package com.tzj.collect.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.lexicon.param.DailyDaParam;
import com.tzj.collect.entity.DailyLexicon;
import com.tzj.collect.entity.Member;

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

     Map<String, Object> weekRecords(Member member);

     List<Map<String, Object>> weekDresserList(Integer startPage, Integer pageSize);

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

}
