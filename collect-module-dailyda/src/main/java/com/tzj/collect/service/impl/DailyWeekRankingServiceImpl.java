package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.DailyWeekRankingMapper;
import com.tzj.collect.entity.DailyWeekRanking;
import com.tzj.collect.service.DailyWeekRankingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @author sgmark
 * @create 2019-08-09 16:08
 **/
@Service
@Transactional(readOnly = true)
public class DailyWeekRankingServiceImpl extends ServiceImpl<DailyWeekRankingMapper, DailyWeekRanking> implements DailyWeekRankingService {

    @Override
    public Page<DailyWeekRanking> eachWeekDresserList(Integer pageNumber, Integer pageSize) {
        Page page = new Page(null == pageNumber ? 1: pageNumber,null ==  pageSize ? 10 :  pageSize);
        return this.selectPage(page, new EntityWrapper<DailyWeekRanking>().eq("del_flag", 0).orderDesc(Arrays.asList("create_date")));
    }
}

