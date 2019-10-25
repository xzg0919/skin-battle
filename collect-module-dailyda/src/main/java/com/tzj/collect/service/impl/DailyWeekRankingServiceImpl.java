package com.tzj.collect.service.impl;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.excel.ExcelData;
import com.tzj.collect.api.commom.excel.ExcelUtils;
import com.tzj.collect.entity.DailyWeekRanking;
import com.tzj.collect.entity.Member;
import com.tzj.collect.mapper.DailyWeekRankingMapper;
import com.tzj.collect.service.DailyLexiconService;
import com.tzj.collect.service.DailyMemberService;
import com.tzj.collect.service.DailyWeekRankingService;
import com.tzj.module.common.file.upload.FileUpload;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.io.*;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author sgmark
 * @create 2019-08-09 16:08
 **/
@Service
@Transactional(readOnly = true)
public class DailyWeekRankingServiceImpl extends ServiceImpl<DailyWeekRankingMapper, DailyWeekRanking> implements DailyWeekRankingService {


    @Resource
    private JedisPool jedisPool;

    @Resource
    private DailyLexiconService dailyLexiconService;

    @Resource
    private DailyMemberService memberService;

    @Autowired
    private FileUpload fileUpload;




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
            dailyWeekRanking = new DailyWeekRanking();
        }
        //周达人aliUserId
//        String tableWeek = LocalDate.now().getYear()+""+(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear()) - 1);
//        String aliUserId = dailyWeekRankingMapper.insertEachWeekDresser("daily_day_records_"+tableWeek);
        Jedis jedis = jedisPool.getResource();
        String redisTableName = redisKeyNameLastWeek() + ":" + "user_input_date";
        //周记录
        Set<Tuple> aliUserIdSet = jedis.zrevrangeByScoreWithScores(redisKeyNameLastWeek(), 1000, 0, 0,100);
        List<Map<String, Object>> aliUserIdScoreList = new ArrayList<>();
        aliUserIdSet.stream().forEach(tuple -> {
            Map<String, Object> tupleMap = new HashMap<>();
            List<String> aliUserIdScore = Arrays.asList(tuple.getElement().replace("[", "").replace("]", "").split(","));
//            tupleMap.put("aliUserId", aliUserIdScore.get(0));
            tupleMap.put("score", tuple.getScore());
            //这里根据阿里userId去找当前用户信息
            Map<String, Object> member = memberService.selectMemberInfoByAliUserId(aliUserIdScore.get(0));
            try {
                tupleMap.put("picUrl", null == member.get("picUrl") ? "" : member.get("picUrl"));
                tupleMap.put("linkName", null == member.get("linkName") ? "" : member.get("linkName"));
                tupleMap.put("mobile", member.get("mobile"));
                tupleMap.put("city", null == member.get("city") ? "" : member.get("city"));
                //            //取出用户的答题时间
                Double userTime = jedis.zscore(redisTableName, aliUserIdScore.get(0));
                tupleMap.put("userInputDate", null == userTime ? Double.POSITIVE_INFINITY : userTime);
            }catch (Exception e){
                System.out.println(e.getCause());
                tupleMap.put("userInputDate", Double.POSITIVE_INFINITY);
            }
            aliUserIdScoreList.add(tupleMap);
        });
//        System.out.println(System.currentTimeMillis()-localTime);
        List<Map<String, Object>> collect = aliUserIdScoreList.stream().sorted(Comparator.comparing(DailyLexiconServiceImpl::comparingByScore).reversed().thenComparing(DailyLexiconServiceImpl::comparingByInputDate)).collect(Collectors.toList());

        Map<String, Object> memberMap = collect.iterator().next();
        dailyWeekRanking.setWeek(week);
        if (!CollectionUtils.isEmpty(memberMap)){
            dailyWeekRanking.setCity(null == memberMap.get("city") ? "" : memberMap.get("city").toString());
            dailyWeekRanking.setImg(null == memberMap.get("picUrl") ? "" :  memberMap.get("picUrl").toString());
            dailyWeekRanking.setLinkName(null == memberMap.get("linkName") ? "" : memberMap.get("linkName").toString());
            dailyWeekRanking.setCreateDate(new Date());
            dailyWeekRanking.setUpdateDate(new Date());
            //计算上周达人
            this.insert(dailyWeekRanking);
        }
        jedis.close();
    }

    /** 每周（1次）执行定时任务：上传上周排行榜记录到oss(供用户下载)
     * @author sgmark@aliyun.com
     * @date 2019/9/4 0004
     * @param
     * @return
     */
    @Override
    public void uploadExcel() {
        List<Map<String, Object>> dailyLastWeekRanking =  dailyLexiconService.weekDresserList();
        ExcelData data = new ExcelData();
        data.setName("Ranking");
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("排名");
        titles.add("昵称");
        titles.add("电话");
        titles.add("分数");
        titles.add("答题次数");
        titles.add("红包领取次数");
        titles.add("总金额");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        AtomicInteger integer = new AtomicInteger(1);
        dailyLastWeekRanking.stream().forEach(dailyLastWeekRankings ->{
            List<Object> row  = new ArrayList();
            row.add(integer.getAndIncrement());
            row.add(dailyLastWeekRankings.get("linkName"));
            row.add(dailyLastWeekRankings.get("mobile"));
            row.add(dailyLastWeekRankings.get("score"));
            row.add(dailyLastWeekRankings.get("sum"));
            row.add(dailyLastWeekRankings.get("count_"));
            row.add(dailyLastWeekRankings.get("price"));
            rows.add(row);
        });
        data.setRows(rows);
        Integer lastWeek = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear())-1;

        String fileName = LocalDate.now().getYear()+"_"+lastWeek+".xls";
        File file = null;
        try {
            file = new File(System.getProperties().getProperty("user.home") + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            ExcelUtils.exportExcel(data, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //上传至oss 用完过后删除本地file
        if (null != file){
            System.out.println(file.getAbsolutePath());
            System.out.println(fileUpload.getImageDomain()+"/"+fileName);
            try {
                uploadExcelFile(fileName, file);
            }catch (Exception e){
                e.printStackTrace();
            }
//            file.delete();
        }
    }

    /**
     * 根据周数获取榜单
     * @author: sgmark@aliyun.com
     * @Date: 2019/10/15 0015
     * @Param: 
     * @return: 
     */
    @Override
    public List<Map<String, Object>> dailyAllTop50Ranking(String finalYear, String week) {
        final Integer[] thisWeek = {Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear())};
        //如果当前时间为1-4周，会取去年的数据
        final String[] year = {LocalDate.now().getYear() + ""};
        if (StringUtils.isEmpty(finalYear)){
            finalYear = LocalDate.now().getYear() + "";
        }
        Map<String, Object> firstMap = new HashMap<>();
        Map<String, Object> secondMap = new HashMap<>();
        Map<String, Object> thirdMap = new HashMap<>();
        Map<String, Object> fourthMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        resultList.add(firstMap);
        resultList.add(secondMap);
        resultList.add(thirdMap);
        resultList.add(fourthMap);

        if (StringUtils.isEmpty(week)){
            //默认上一周
            week = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().minusWeeks(1).get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear()) +"";
        }
//        List<Map<String, Object>> maps = dailyLexiconService.weekRankingTop50(jedisPool.getResource(), finalYear + ":" + week);
//        maps = maps.stream().sorted(Comparator.comparing(DailyLexiconServiceImpl::comparingByScore).reversed().thenComparing(DailyLexiconServiceImpl::comparingByInputDate)).limit(50).collect(Collectors.toList());
        String finalWeek = week;
//        List<Map<String, Object>> finalMaps = maps;
        //放入查出来的前50数据
        AtomicInteger integer = new AtomicInteger(1);
        resultList.stream().forEach(resultLists ->{
            Integer increment = integer.getAndIncrement();
            if (thisWeek[0] - increment <= 0){
                year[0] = LocalDate.now().getYear()-1 +"";
                thisWeek[0] = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().minusWeeks(increment).get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear()) + increment;
            }
            resultLists.put("week", thisWeek[0] -increment);
            resultLists.put("year", year[0]);
            resultLists.put("yearWeek", year[0] +"年第"+(thisWeek[0] -increment)+"周");

//            if (resultLists.get("week").toString().equals(finalWeek)){
//                resultLists.put("top50List", "");
//            }else {
//            }
            resultLists.put("top50List", "");
        });
        return resultList;
    }

    @Override
    public List<Map<String, Object>> dailyPersonRanking(Member member) {

        final Integer[] thisWeek = {Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear())};
        //如果当前时间为1-4周，会取去年的数据
        final String[] year = {LocalDate.now().getYear() + ""};
        Map<String, Object> firstMap = new HashMap<>();
        Map<String, Object> secondMap = new HashMap<>();
        Map<String, Object> thirdMap = new HashMap<>();
        Map<String, Object> fourthMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        resultList.add(firstMap);
        resultList.add(secondMap);
        resultList.add(thirdMap);
        resultList.add(fourthMap);
        //默认上一周
        //放入查出来的前50数据
        AtomicInteger integer = new AtomicInteger(1);
        resultList.stream().forEach(resultLists ->{
            Integer increment = integer.getAndIncrement();
            if (thisWeek[0] - increment <= 0){
                year[0] = LocalDate.now().getYear()-1 +"";
                thisWeek[0] = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime().now().minusWeeks(increment).get(WeekFields.of(DayOfWeek.MONDAY,1).weekOfYear()) + increment;
            }
            resultLists.put("week", thisWeek[0] -increment);
            resultLists.put("year", year[0]);
            resultLists.put("yearWeek", year[0] +"年第"+(thisWeek[0] -increment)+"周");
            resultLists.put("ranking", dailyLexiconService.weekRankingByTime(jedisPool.getResource(), member, year[0]+":"+(thisWeek[0] -increment)));
            resultLists.put("picUrl", null == member.getPicUrl() ? "": member.getPicUrl());
            resultLists.put("linkName", null == member.getLinkName() ? "" : member.getLinkName());
        });
        return resultList;
    }

    @Override
    @Cacheable(value = "dailyTop50" , key = "#year + '_' + #week",   sync = true)
    public List<Map<String, Object>> dailyAllTop50RankingTime(String year, String week) {
        if (StringUtils.isEmpty(year) ||  StringUtils.isEmpty(week)){
            throw new ApiException("参数错误");
        }
        List<Map<String, Object>> maps = dailyLexiconService.weekRankingTop50(jedisPool.getResource(), year + ":" + week);
        maps = maps.stream().sorted(Comparator.comparing(DailyLexiconServiceImpl::comparingByScore).reversed().thenComparing(DailyLexiconServiceImpl::comparingByInputDate)).limit(50).collect(Collectors.toList());
        return maps;
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

    public void  uploadExcelFile(String url, File file){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-shanghai.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAIMbbuj3E2uX48";
        String accessKeySecret = "V8RPkZqqaBg6QK0mk9GsPcub8ePRyN";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ossClient.putObject("osssqt", url, inputStream);

// 关闭OSSClient。
        ossClient.shutdown();
    }
}

