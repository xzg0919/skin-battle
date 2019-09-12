package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.lexicon.param.DailyDaParam;
import com.tzj.collect.api.lexicon.param.MemberBean;
import com.tzj.collect.api.lexicon.utils.JedisUtil;
import com.tzj.collect.mapper.DailyLexiconMapper;
import com.tzj.collect.entity.DailyLexicon;
import com.tzj.collect.entity.Member;
import com.tzj.collect.service.DailyLexiconService;
import com.tzj.collect.service.DailyMemberService;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author sgmark
 * @create 2019-08-09 16:08
 **/
@Service
@Transactional
public class DailyLexiconServiceImpl extends ServiceImpl<DailyLexiconMapper, DailyLexicon> implements DailyLexiconService {

//    @Resource(name = "collectJedisPool")
    @Resource
    private JedisPool jedisPool;

    @Resource
    private DailyMemberService dailyMemberService;

    @Resource
    private  DailyLexiconMapper dailyLexiconMapper;

    @Resource
    private DailyLexiconService dailyLexiconService;

    /** 查询当前用户当天在本周记录表中是否有数据若没有，创建答题，若已有直接返回当前用户题目信息
      * @author sgmark@aliyun.com
      * @date 2019/8/17 0017
      * @param
      * @return
      */
    public  Set<Map<String, Object>> isAnswerDaily(String aliUserId){
        //本周记录表若没有创建新表
        if(dailyLexiconMapper.existTable(tableName(System.currentTimeMillis())) <= 0){
            dailyLexiconMapper.createNewTable(tableName(System.currentTimeMillis()));
            return new HashSet<>();
        }
        Set<Map<String, Object>> returnList = new HashSet<>();
        //查询当前用户当天在本周记录表中是否有数据若没有，创建答题，若已有直接返回当前用户题目信息
        returnList = dailyLexiconMapper.isAnswerDaily(aliUserId, tableName(System.currentTimeMillis()), LocalDate.now()+" 00:00:00", LocalDate.now() + " 23:59:59");
        return returnList;
    }
    /**  各个随机取2值
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    @Override
    public Set<Map<String, Object>> dailyLexiconList(Member member) {
        System.out.println(System.currentTimeMillis());
        String aliUserId = member.getAliUserId();
        Set<Map<String, Object>> returnList = new HashSet<>();
        returnList = dailyLexiconMapper.dailyLexiconList(aliUserId, tableName(System.currentTimeMillis()), LocalDate.now()+" 00:00:00", LocalDate.now() + " 23:59:59");
        if (returnList.size() > 0){
            returnList.stream().forEach(returnLists -> returnLists.put("answerList", answerMapList()));
            return returnList;
        }
        List<Map<String, Object>> mapList = dailyLexiconService.lexiconList("lexiconList");
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
//            returnLists.remove("id");
//            returnLists.remove("type_id");
            returnLists.put("answerList", answerMapList());
        });
        //每天生成题目，把当前时间存入redis，视为用户开始答题时间
        Jedis jedis = jedisPool.getResource();
        jedis.zadd(redisKeyName() +":"+"user_input_date", System.currentTimeMillis(),aliUserId);
        //用户答题保存用户信息（头像、城市、昵称）
        JedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new JedisUtil.SaveOrGetFromRedis();
        MemberBean memberBean = new MemberBean();
        memberBean.setCity(member.getCity());
        memberBean.setLinkName(member.getLinkName());
        memberBean.setPicUrl(member.getPicUrl());
        memberBean.setMobile(member.getMobile());

//        保存个人信息到redis，时间为一周
        saveOrGetFromRedis.saveInRedis(redisKeyName() +":"+"user_information"+":"+aliUserId, memberBean, 7*24*3600, jedisPool, 0);

        jedis.close();
        System.out.println(System.currentTimeMillis());
        return returnList;
    }

    public  List<Map<String, Object>> answerMapList(){
        //给出答案选项，包含正确答案
        List<Map<String, Object>> answerList = new ArrayList<>();
        Map<String, Object> answerMap = new HashMap<>();

        answerMap.put("typeId", "1");
        answerMap.put("name", "干垃圾");
        answerMap.put("aliasName", "DRY");
        answerList.add(answerMap);

        answerMap = new HashMap<>();
        answerMap.put("typeId", "2");
        answerMap.put("name", "湿垃圾");
        answerMap.put("aliasName", "KITCHEN");
        answerList.add(answerMap);

        answerMap = new HashMap<>();
        answerMap.put("typeId", "3");
        answerMap.put("name", "可回收物");
        answerMap.put("aliasName", "RECYCLABLE");
        answerList.add(answerMap);

        answerMap = new HashMap<>();
        answerMap.put("typeId", "4");
        answerMap.put("name", "有害垃圾");
        answerMap.put("aliasName", "HARMFUL");
        answerList.add(answerMap);
        Collections.shuffle(answerList);//打乱顺序
        return answerList;
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
        Jedis jedis = jedisPool.getResource();
        if (StringUtils.isEmpty(dailyDaParam.getAliUserId())|| StringUtils.isEmpty(dailyDaParam.getUuId()) || null == dailyDaParam.getLexType() || 0 == dailyDaParam.getDepth()){
            throw new ApiException("参数错误，提交失败");
        }else if (dailyLexiconMapper.isAnswerDaily(dailyDaParam.getAliUserId(), tableName(System.currentTimeMillis()), LocalDate.now()+" 00:00:00", LocalDate.now() + " 23:59:59").size() >= 10){
            throw new ApiException("每日答题，只能为十题");
        }
        Map<String, Object> returnMap = new HashMap<>();
        AtomicReference<String> trueOrFalseReturn = new AtomicReference<>("N");
        //所有题目信息(走服务器缓存)(改：走数据库)
//        List<Map<String, Object>> mapList = dailyLexiconService.lexiconList(dailyDaParam.getAliUserId());
        List<Map<String, Object>> mapList = dailyLexiconMapper.lexiconListByAliUserId(dailyDaParam.getAliUserId(), tableName(System.currentTimeMillis()), LocalDate.now()+" 00:00:00", LocalDate.now() + " 23:59:59");
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
                            //保存用户答题信息(只允许答今日所在题目且只能答一次)
                            if (dailyLexiconMapper.updateDailyRecordsList(dailyDaParam.getAliUserId(), tableName(System.currentTimeMillis()) ,dailyDaParam.getUuId(), dailyDaParam.getLexType().getValue(),trueOrFalse, LocalDate.now()+" 00:00:00", LocalDate.now() + " 23:59:59") > 0){
                                if ("0".equals(trueOrFalse)){
                                    //正确答题积分加十(周记录)
                                    jedis.zincrby(redisKeyName(), 10, dailyDaParam.getAliUserId());
                                    //每日答题数据记录
                                    jedis.zincrby(redisKeyName() +":"+ LocalDate.now().getDayOfWeek(), 10, dailyDaParam.getAliUserId());
//                                System.out.println(jedisPool.getResource().zscore(redisKeyName(), dailyDaParam.getAliUserId()));
                                }else{
                                    //正确答题积分加十(周记录)
                                    jedis.zincrby(redisKeyName(), 0, dailyDaParam.getAliUserId());
                                    //每日答题数据记录
                                    jedis.zincrby(redisKeyName() +":"+ LocalDate.now().getDayOfWeek(), 0, dailyDaParam.getAliUserId());
//
                                }
                            }
                        }else {
                            throw new ApiException("该题已被答过");
                        }
                    }
                });
            }
        });
        returnMap.put("isTrue", trueOrFalseReturn.get());
        jedis.close();
        return returnMap;
    }


    /**(名称头像，本周得分，排名)
      * @author sgmark@aliyun.com
      * @date 2019/8/14 0014
      * @param
      * @return
      */
    @Override
    public Map<String, Object> memberInfo(Member member) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("linkName", member.getLinkName());
        returnMap.put("picUrl", member.getPicUrl());
        //每周分数
        Jedis jedis = jedisPool.getResource();
        Long localTime = System.currentTimeMillis();
        Double weekScore = jedis.zscore(redisKeyName(), member.getAliUserId());
        returnMap.put("weekScore", null == weekScore ? 0: weekScore.intValue());
        //当前周排名
        Long weekRanking = jedis.zrevrank(redisKeyName(), member.getAliUserId());
        returnMap.put("weekRanking", null == weekRanking ? "999+": ++weekRanking);
        //本日是否已答过题
        Long zrank = jedis.zrank(redisKeyName() + ":" + LocalDate.now().getDayOfWeek(), member.getAliUserId());
        if (null == zrank){
            returnMap.put("todayIsAnswer", "N");
        }else {
            returnMap.put("todayIsAnswer", "Y");
        }
        System.out.println(System.currentTimeMillis()-localTime);
        jedis.close();
//        returnMap.put("todayIsAnswer", dailyLexiconMapper.isAnswerDaily(member.getAliUserId(), tableName(System.currentTimeMillis()), LocalDate.now()+" 00:00:00", LocalDate.now() + " 23:59:59").size() > 0 ? "Y":"N");
        return returnMap;
    }

    @Override
    public Map<String, Object> weekRecords(Member member) {
        Map<String, Object> returnMap = new HashMap<>();
        Jedis jedis = jedisPool.getResource();
        //周记录
        Double weekScore = jedis.zscore(redisKeyName(), member.getAliUserId());
        returnMap.put("weekScore", null == weekScore ? 0: weekScore.intValue());
        //当前周排名
        Long weekRanking = jedis.zrevrank(redisKeyName(), member.getAliUserId());
        returnMap.put("weekRanking", null == weekRanking ? "999+": ++weekRanking);
        Double todayScore = jedis.zscore(redisKeyName()+":"+LocalDate.now().getDayOfWeek(), member.getAliUserId());
        returnMap.put("todayScore", null == todayScore ? 0 : todayScore.intValue());
        returnMap.put("todayTrue", null == todayScore ? 0 : todayScore.intValue()/10);
        returnMap.put("todayFalse", null == todayScore ? 10 : 10 - todayScore.intValue()/10);
        jedis.close();
        return returnMap;
    }

    /** 本周达人榜(头像，得分，名次), 需要分页（包含top50的数据， page最大值取到(假设每页10条，最大页数取值为5)）
     * @author sgmark@aliyun.com
     * @date 2019/8/12 0012
     * @param
     * @return
     */
    @Override
    public List<Map<String, Object>> weekDresserList(Integer startPage, Integer pageSize) {
        Integer limit = 10;
        if (startPage == 1 && pageSize == 3){
            limit = 3;
        }
        Jedis jedis = jedisPool.getResource();
        Long localTime = System.currentTimeMillis();
        String redisTableName = redisKeyName() + ":" + "user_input_date";
//        //先取第50名分数，取出所有大于等于此分数的人，据此排名
//        Set<String> stringSet = jedis.zrevrangeByScore(redisTableName, 50, 50);
//        Double zscore = jedis.zscore(redisTableName, stringSet.iterator().next());
        //取出大于此分数所有人
//        Set<Tuple> aliUserIdSet = jedis.zrevrangeByScoreWithScores(redisKeyName(), 1000, zscore, (startPage - 1) * pageSize,pageSize);
        //周记录 取出前100名成员
        Set<Tuple> aliUserIdSet = jedis.zrevrangeByScoreWithScores(redisKeyName(), 1000, 0, 0, 5*limit+5);
        List<Map<String, Object>> aliUserIdScoreList = new ArrayList<>();
        JedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new JedisUtil.SaveOrGetFromRedis();
        aliUserIdSet.stream().forEach(tuple -> {
            Map<String, Object> tupleMap = new HashMap<>();
            List<String> aliUserIdScore = Arrays.asList(tuple.getElement().replace("[", "").replace("]", "").split(","));
//            tupleMap.put("aliUserId", aliUserIdScore.get(0));
            tupleMap.put("score", tuple.getScore());
            //这里根据阿里userId去找当前用户信息
//            Map<String, Object> member = dailyMemberService.selectMemberInfoByAliUserId();
            System.out.println("-------------------------------------------------");
            System.out.println(System.currentTimeMillis());
            MemberBean member = (MemberBean) saveOrGetFromRedis.getFromRedis(redisKeyName()+":"+"user_information"+":"+aliUserIdScore.get(0),jedisPool, 0);
            System.out.println(System.currentTimeMillis());
            if (null == member){
                member = new MemberBean();
                Map<String, Object> map = dailyMemberService.selectMemberInfoByAliUserId(aliUserIdScore.get(0));
                tupleMap.put("picUrl", null == map.get("picUrl") ? "": map.get("picUrl"));
                tupleMap.put("linkName", null == map.get("linkName") ? "" : map.get("linkName"));
//                tupleMap.put("mobile", map.get("mobile"));
                tupleMap.put("city", null == map.get("city") ? "" : map.get("city"));

                member.setCity(map.get("city")+"");
                member.setLinkName(map.get("linkName")+"");
                member.setPicUrl(map.get("city")+"");
                member.setMobile(map.get("picUrl")+"");

                //没查到保存至redis
                saveOrGetFromRedis.saveInRedis(redisKeyName()+":"+"user_information"+":"+aliUserIdScore.get(0), member, 7*24*3600, jedisPool,0);

            }else {
                tupleMap.put("picUrl", null == member.getPicUrl() ? "": member.getPicUrl());
                tupleMap.put("linkName", null == member.getLinkName() ? "" : member.getLinkName());
                tupleMap.put("city", null == member.getCity() ? "" : member.getCity());
            }
//            //取出用户的答题时间
            Double userTime = jedis.zscore(redisTableName, aliUserIdScore.get(0));
            tupleMap.put("userInputDate", null == userTime ? Double.POSITIVE_INFINITY  : userTime);
            aliUserIdScoreList.add(tupleMap);
        });
        System.out.println(System.currentTimeMillis()-localTime);
        //根据redis中答题时间（谁先答题，同分数下谁在前面）
        List<Map<String, Object>> collect = aliUserIdScoreList.stream().sorted(Comparator.comparing(DailyLexiconServiceImpl::comparingByScore).reversed().thenComparing(DailyLexiconServiceImpl::comparingByInputDate)).limit(limit).collect(Collectors.toList());
        //关闭 jedis
        jedis.close();
        System.out.println(System.currentTimeMillis()-localTime);
        return collect;
    }

    private static Double comparingByScore(Map<String, Object> map){
        return (Double) map.get("score");
    }

    private static Double comparingByInputDate(Map<String, Object> map){
        return (Double) map.get("userInputDate");
    }

    @Override
    public List<Map<String, Object>> weekDresserList() {
        Jedis jedis = jedisPool.getResource();
        String redisTableName = redisKeyName() + ":" + "user_input_date";
        Long localTime = System.currentTimeMillis();
        //周记录
        Set<Tuple> aliUserIdSet = jedis.zrevrangeByScoreWithScores(redisKeyNameLastWeek(), 1000, 0, 0,20000000);
        List<Map<String, Object>> aliUserIdScoreList = new ArrayList<>();
        aliUserIdSet.stream().forEach(tuple -> {
            Map<String, Object> tupleMap = new HashMap<>();
            List<String> aliUserIdScore = Arrays.asList(tuple.getElement().replace("[", "").replace("]", "").split(","));
//            tupleMap.put("aliUserId", aliUserIdScore.get(0));
            tupleMap.put("score", tuple.getScore());
            //这里根据阿里userId去找当前用户信息
            Map<String, Object> member = dailyMemberService.selectMemberInfoByAliUserId(aliUserIdScore.get(0));
            Map<String, Object> sums = dailyLexiconMapper.selectSums(tableNameLastWeek(System.currentTimeMillis()), aliUserIdScore.get(0));
            Map<String, Object> prices = dailyLexiconMapper.selectPrices(aliUserIdScore.get(0), LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY) + "", LocalDate.now().minusWeeks(1).with(DayOfWeek.SUNDAY)+ " 23:59:59");
            if(!CollectionUtils.isEmpty(prices)){
                tupleMap.put("price", null == prices.get("price") ? "" : prices.get("price"));
                tupleMap.put("count_", null == prices.get("count_") ? "" : prices.get("count_"));

            }
            if (!CollectionUtils.isEmpty(sums)){
                tupleMap.put("sum", null == sums.get("sum_") ? 0: sums.get("sum_"));
            }
            tupleMap.put("picUrl", null == member.get("picUrl") ? "": member.get("picUrl"));
            tupleMap.put("linkName", null == member.get("linkName") ? "" : member.get("linkName"));
            tupleMap.put("mobile", member.get("mobile"));
            tupleMap.put("city", null == member.get("city") ? "" : member.get("city"));
            //            //取出用户的答题时间
            Double userTime = jedis.zscore(redisTableName, aliUserIdScore.get(0));
            tupleMap.put("userInputDate", null == userTime ? Double.POSITIVE_INFINITY  : userTime);
            aliUserIdScoreList.add(tupleMap);
        });
        System.out.println(System.currentTimeMillis()-localTime);
        List<Map<String, Object>> collect = aliUserIdScoreList.stream().sorted(Comparator.comparing(DailyLexiconServiceImpl::comparingByScore).reversed().thenComparing(DailyLexiconServiceImpl::comparingByInputDate)).collect(Collectors.toList());
        jedis.close();
        System.out.println(System.currentTimeMillis()-localTime);
        return collect;
    }


    @Override
    public Map<String, Object> errorLexiconList(Member member) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("errorList", dailyLexiconMapper.errorLexiconList(tableName(System.currentTimeMillis()), member.getAliUserId(), LocalDate.now()+" 00:00:00", LocalDate.now() + " 23:59:59"));
        return returnMap;
    }

    /** 保存用户每日答题题目信息
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    public void saveDailyRecordsList(String aliUserId, String uuId, Long lexiconId, Integer typeId){
        //存入每日答题信息
        dailyLexiconMapper.insertDailyRecords(aliUserId, uuId, lexiconId, typeId, tableName(System.currentTimeMillis()));
    }

    /** 根据毫秒数查表名称
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param 
      * @return 
      */
    public static String tableName(Long currentTimeMillis){
        Integer week = Instant.ofEpochMilli(currentTimeMillis).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
        return "daily_day_records_"+ LocalDate.now().getYear() + "" + week;
    }

    public static String tableNameLastWeek(Long currentTimeMillis){
        Integer week = Instant.ofEpochMilli(currentTimeMillis).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().minusWeeks(1l).get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
        return "daily_day_records_"+ LocalDate.now().getYear() + "" + week;
    }

    /** 年:周(作为排序key)
      * @author sgmark@aliyun.com
      * @date 2019/8/13 0013
      * @param
      * @return
      */
    public static String redisKeyName(){
        Integer week = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
        return LocalDate.now().getYear() + ":" + week;
    }

    public static String redisKeyNameLastWeek(){
        Integer week = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear())-1;
        return LocalDate.now().getYear() + ":" + week;
    }

    public static void main(String[] args) {
        //计算周数
//        LocalDateTime.now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear());
        System.out.println(LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY));
        System.out.println(LocalDate.now().minusWeeks(1).with(DayOfWeek.SUNDAY));

    }
}

