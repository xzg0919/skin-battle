package com.tzj.collect.core.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.DailyWeekRanking;
import com.tzj.collect.entity.Member;

import java.util.List;
import java.util.Map;


/**
 * @Author sgmark@aliyun.com
 **/
public interface DailyWeekRankingService extends IService<DailyWeekRanking> {

     @DS("slave")
     Page<DailyWeekRanking> eachWeekDresserList(Integer pageNumber, Integer pageSize);
     /**  计算前一周的环保荣誉达人
       * @author sgmark@aliyun.com
       * @date 2019/8/19 0019
       * @param
       * @return
       */
     @DS("slave")
     void insertEachWeekDresser();
     //上传excel文档到oss
     @DS("slave")
     void uploadExcel();

    List<Map<String, Object>> dailyAllTop50Ranking(String year, String week);

     List<Map<String, Object>> dailyPersonRanking(Member member);

     List<Map<String, Object>> dailyAllTop50RankingTime(String year, String week);
}
