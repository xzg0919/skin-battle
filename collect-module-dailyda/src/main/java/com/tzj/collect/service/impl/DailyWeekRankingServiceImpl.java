package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.DailyWeekRankingMapper;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.entity.DailyWeekRanking;
import com.tzj.collect.entity.Member;
import com.tzj.collect.service.DailyWeekRankingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Set;

/**
 * @author sgmark
 * @create 2019-08-09 16:08
 **/
@Service
@Transactional(readOnly = true)
public class DailyWeekRankingServiceImpl extends ServiceImpl<DailyWeekRankingMapper, DailyWeekRanking> implements DailyWeekRankingService {


    @Resource
    private DailyWeekRankingMapper dailyWeekRankingMapper;

    @Resource
    private JedisPool jedisPool;


    @Resource
    private MemberService memberService;

    @Override
    public Page<DailyWeekRanking> eachWeekDresserList(Integer pageNumber, Integer pageSize) {
        Page page = new Page(null == pageNumber ? 1: pageNumber,null ==  pageSize ? 10 :  pageSize);
        return this.selectPage(page, new EntityWrapper<DailyWeekRanking>().eq("del_flag", 0).orderDesc(Arrays.asList("create_date")));
    }

    @Override
    @Transactional(readOnly = false)
    public void insertEachWeekDresser() {
        //当前时间上一周
        String week = LocalDate.now().getYear()+"年第"+(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear()) - 1)+"周";
        DailyWeekRanking dailyWeekRanking = this.selectOne(new EntityWrapper<DailyWeekRanking>().eq("del_flag", 0).eq("week_", week));
        if (null == dailyWeekRanking) {
            dailyWeekRanking = new DailyWeekRanking();
        }else {
            return;
        }
        //周达人aliUserId
        String tableWeek = LocalDate.now().getYear()+""+(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear()) - 1);
        String aliUserId = dailyWeekRankingMapper.insertEachWeekDresser("daily_day_records_"+tableWeek);
        //以redis默认排序为准
        Set<String> stringSet = jedisPool.getResource().zrevrangeByScore(this.redisKeyNameLastWeek(), 1000, 0);
        Member member = memberService.selectMemberByAliUserId(stringSet.iterator().next().trim());
        dailyWeekRanking.setWeek(week);
        dailyWeekRanking.setCity(null==member.getCity()?"":member.getCity());
        dailyWeekRanking.setImg(member.getPicUrl());
        dailyWeekRanking.setLinkName(member.getLinkName());
        //计算上周达人
        this.insert(dailyWeekRanking);
    }
    /** 上周达人榜
      * @author sgmark@aliyun.com
      * @date 2019/8/19 0019
      * @param
      * @return
      */
    public static String redisKeyNameLastWeek(){
        Integer week = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear())-1;
        return LocalDate.now().getYear() + ":" + week;
    }

}

