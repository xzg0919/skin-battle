package com.tzj.collect.core.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.DailyWeekRanking;
import org.apache.ibatis.annotations.Param;

/**
  * @author sgmark@aliyun.com
  * @date 2019/8/15 0015
  * @param
  * @return
  */
public interface DailyWeekRankingMapper extends BaseMapper<DailyWeekRanking> {

    String insertEachWeekDresser(@Param("tableName") String week);
}
