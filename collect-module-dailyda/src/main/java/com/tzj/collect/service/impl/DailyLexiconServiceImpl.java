package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.lexicon.param.DailyDaParam;
import com.tzj.collect.core.mapper.DailyLexiconMapper;
import com.tzj.collect.entity.DailyLexicon;
import com.tzj.collect.service.DailyLexiconService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * @author sgmark
 * @create 2019-08-09 16:08
 **/
@Service
@Transactional
public class DailyLexiconServiceImpl extends ServiceImpl<DailyLexiconMapper, DailyLexicon> implements DailyLexiconService {

    @Resource
    private  DailyLexiconMapper dailyLexiconMapper;
    /**  各个随机取2值
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    @Override
    @Cacheable(value = "dailyLexiconList", key = "#aliUserId", sync = true)
    public Set<Map<String, Object>> dailyLexiconList(String aliUserId) {
        Set<Map<String, Object>> returnList = new HashSet<>();
        //查询当前用户当天在本周记录表中是否有数据若没有，创建答题，若已有直接返回当前用户题目信息  todo

        List<Map<String, Object>> mapList = lexiconList("lexiconList");
        //设置开始时间
        Long startTime = System.currentTimeMillis();
        while (returnList.size() < 10 && System.currentTimeMillis() - startTime <= 200) {
            mapList.stream().forEach(mapLists -> {
                int size = returnList.size();
                if (size > 10) {
                    return;
                }
                List<Object> depthLists = (ArrayList) mapLists.get("depthList");
                //未考虑list大小只为1的情况(会出现死循环)
                if (depthLists.size() == 1) {
                    returnList.add((Map<String, Object>) depthLists.get(0));
                } else {
                    while (returnList.size() - size < 2 && returnList.size() != 10) {
                        returnList.add((Map<String, Object>) depthLists.get(new Random().nextInt(depthLists.size())));
                    }
                }
            });
        }

        //去除答案
        returnList.stream().forEach(returnLists ->{
            //用户点击开始答题，直接存入本周记录
            String uuId = UUID.randomUUID().toString();
            //新开线程保存记录
            this.saveDailyRecordsList(aliUserId, uuId, Long.parseLong(returnLists.get("id").toString()), Integer.parseInt(returnLists.get("type_id").toString()));
            returnLists.put("uuId", uuId);
            returnLists.remove("id");
            returnLists.remove("type_id");
        });
        return returnList;
    }


    /**   总的list
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    @Override
    @Cacheable(value = "dailyLexiconList", key = "#lexiconList", sync = true)
    public List<Map<String, Object>> lexiconList(String lexiconList) {
        return dailyLexiconMapper.lexiconList();
    }

    @Override
    public Map<String, Object> lexiconChecking(DailyDaParam dailyDaParam) {
        List<Map<String, Object>> mapList = lexiconList("lexiconList");
        //查找用户所对应的题目信息
        final Set<Map<String, Object>> maps = dailyLexiconList(dailyDaParam.getAliUserId());
        return null;
    }

    /** 保存用户每日答题题目信息
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    public void saveDailyRecordsList(String aliUserId, String uuId, Long lexiconId, Integer typeId){
        dailyLexiconMapper.insertDailyRecords(aliUserId, uuId, lexiconId, typeId, tableName(System.currentTimeMillis()));
    }

    /** 根据毫秒数查表名称
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param 
      * @return 
      */
    public String tableName(Long currentTimeMillis){
        Integer week = Instant.ofEpochMilli(currentTimeMillis).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
        return "daily_day_records_"+ LocalDate.now().getYear() + "" + week;
    }

//    public static void main(String[] args) {
//        //计算周数
////        LocalDateTime.now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
////        System.out.println(tableName(System.currentTimeMillis()));
//
//    }
}

