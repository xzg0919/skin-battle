package com.tzj.collect.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.DailyWeekRanking;



/**
 * @Author sgmark@aliyun.com
 **/
public interface DailyWeekRankingService extends IService<DailyWeekRanking> {

     Page<DailyWeekRanking> eachWeekDresserList(Integer pageNumber, Integer pageSize);
     /**  计算前一周的环保荣誉达人
       * @author sgmark@aliyun.com
       * @date 2019/8/19 0019
       * @param
       * @return
       */
     void insertEachWeekDresser();
}
