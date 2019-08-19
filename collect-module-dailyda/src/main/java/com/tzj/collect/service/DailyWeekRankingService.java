package com.tzj.collect.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.DailyWeekRanking;



/**
 * @Author sgmark@aliyun.com
 **/
public interface DailyWeekRankingService extends IService<DailyWeekRanking> {

     Page<DailyWeekRanking> eachWeekDresserList(Integer pageNumber, Integer pageSize);
}
