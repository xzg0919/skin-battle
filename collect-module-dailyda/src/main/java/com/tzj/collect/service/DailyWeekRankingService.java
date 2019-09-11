package com.tzj.collect.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.DailyWeekRanking;



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
}
