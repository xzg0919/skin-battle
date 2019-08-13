package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.lexicon.param.DailyDaParam;
import com.tzj.collect.core.mapper.DailyLexiconMapper;
import com.tzj.collect.entity.DailyLexicon;
import com.tzj.collect.service.DailyLexiconService;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author sgmark
 * @create 2019-08-09 16:08
 **/
@Service
@Transactional
public class DailyLexiconServiceImpl extends ServiceImpl<DailyLexiconMapper, DailyLexicon> implements DailyLexiconService {

    @Resource
    private JedisPool jedisPool;

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
        returnList = dailyLexiconMapper.dailyLexiconList(aliUserId, tableName(System.currentTimeMillis()), LocalDate.now()+" 00:00:00", LocalDate.now() + " 23:59:59");
        if (returnList.size() > 0){
            return returnList;
        }
        List<Map<String, Object>> mapList = lexiconList("lexiconList");
        //设置开始时间
        Long startTime = System.currentTimeMillis();
        while (returnList.size() < 10 && System.currentTimeMillis() - startTime <= 200) {
            Set<Map<String, Object>> finalReturnList = returnList;
            mapList.stream().forEach(mapLists -> {
                int size = finalReturnList.size();
                if (size > 10) {
                    return;
                }
                List<Object> depthLists = (ArrayList) mapLists.get("depthList");
                //未考虑list大小只为1的情况(会出现死循环)
                if (depthLists.size() == 1) {
                    finalReturnList.add((Map<String, Object>) depthLists.get(0));
                } else {
                    while (finalReturnList.size() - size < 2 && finalReturnList.size() != 10) {
                        finalReturnList.add((Map<String, Object>) depthLists.get(new Random().nextInt(depthLists.size())));
                    }
                }
            });
        }
        //去除答案
        returnList.stream().forEach(returnLists ->{
            //用户点击开始答题，直接存入本周记录
            String uuId = UUID.randomUUID().toString();
            //保存记录
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
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> lexiconChecking(DailyDaParam dailyDaParam) {
        if (StringUtils.isEmpty(dailyDaParam.getAliUserId())|| StringUtils.isEmpty(dailyDaParam.getUuId()) || null == dailyDaParam.getLexType() || 0 == dailyDaParam.getDepth()){
            throw new ApiException("参数错误，提交失败");
        }
        Map<String, Object> returnMap = new HashMap<>();
        AtomicReference<String> trueOrFalseReturn = new AtomicReference<>("N");
        //所有题目信息
        List<Map<String, Object>> mapList = lexiconList("lexiconList");
        mapList.stream().forEach(mapLists ->{
            //根据难易程度去找题目所在位置，减少循环
            if(dailyDaParam.getDepth() == Integer.parseInt(mapLists.get("depth").toString())){
                List<Map<String, Object>> depthLists = (ArrayList) mapLists.get("depthList");
                depthLists.stream().forEach(depthList -> {
                    String trueOrFalse = "1";//默认不正确
                    if (dailyDaParam.getLexName().equals(depthList.get("name_").toString())){
                        if (depthList.get("type_id") == dailyDaParam.getLexType().getValue()){
                            //答题正确
                            trueOrFalse  = "0";
                            trueOrFalseReturn.set("Y");
                        }
                        //查询该题是否呗答过
                        if (!CollectionUtils.isEmpty(dailyLexiconMapper.checkDailyIsAnswer(dailyDaParam.getUuId(), tableName(System.currentTimeMillis())))) {
                           //这里如果用户回答正确可以加分(其他地方执行会重复加分)
                            if ("0".equals(trueOrFalse)){
                                //正确答题积分加一
                                jedisPool.getResource().zincrby(redisKeyName(), 1, dailyDaParam.getAliUserId());
//                                System.out.println(jedisPool.getResource().zscore(redisKeyName(), dailyDaParam.getAliUserId()));
                            }
                            //保存用户答题信息(只允许答今日所在题目且只能答一次)
                            dailyLexiconMapper.updateDailyRecordsList(dailyDaParam.getAliUserId(), tableName(System.currentTimeMillis()) ,dailyDaParam.getUuId(), dailyDaParam.getLexType().getValue(),trueOrFalse, LocalDate.now()+" 00:00:00", LocalDate.now() + " 23:59:59");
                        }else {
                            throw new ApiException("该题已被答过");
                        }
                    }
                });
            }
        });
        returnMap.put("isTrue", trueOrFalseReturn.get());
        return returnMap;
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
    /** 年:周(作为排序key)
      * @author sgmark@aliyun.com
      * @date 2019/8/13 0013
      * @param
      * @return
      */
    public String redisKeyName(){
        Integer week = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
        return LocalDate.now().getYear() + ":" + week;
    }

//    public static void main(String[] args) {
//        //计算周数
////        LocalDateTime.now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
////        System.out.println(tableName(System.currentTimeMillis()));
//
//    }
}

