package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.mapper.DailyWeekRankingMapper;
import com.tzj.collect.entity.DailyWeekRanking;
import com.tzj.collect.service.DailyWeekRankingService;
import com.tzj.collect.service.DailyMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Map;
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
    private DailyMemberService memberService;

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
        Jedis jedis = jedisPool.getResource();
        //以redis默认排序为准
        Set<String> stringSet = jedis.zrevrangeByScore(this.redisKeyNameLastWeek(), 1000, 0);
        Map<String, Object> memberMap = memberService.selectMemberInfoByAliUserId(stringSet.iterator().next().trim());
        dailyWeekRanking.setWeek(week);
        if (!CollectionUtils.isEmpty(memberMap)){
            dailyWeekRanking.setCity(null == memberMap.get("city") ? "" : memberMap.get("city").toString());
            dailyWeekRanking.setImg(null == memberMap.get("picUrl") ? "" :  memberMap.get("picUrl").toString());
            dailyWeekRanking.setLinkName(null == memberMap.get("linkName") ? "" : memberMap.get("linkName").toString());
        }
        //计算上周达人
        this.insert(dailyWeekRanking);
        jedis.close();
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

