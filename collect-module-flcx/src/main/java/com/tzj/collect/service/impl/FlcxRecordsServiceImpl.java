package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.entity.FlcxType;
import com.tzj.collect.mapper.FlcxRecordsMapper;
import com.tzj.collect.mapper.FlcxTypeMapper;
import com.tzj.collect.module.common.shard.ShardTableHelper;
import com.tzj.collect.service.FlcxRecordsService;
import com.tzj.collect.service.FlcxTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
@Service
@Transactional(readOnly = true)
public class FlcxRecordsServiceImpl extends ServiceImpl<FlcxRecordsMapper, FlcxRecords> implements FlcxRecordsService {

    @Resource
    private FlcxRecordsMapper flcxRecordsMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Cacheable(value = "flcxTopFive", key = "#topFive", sync = true)
    public Map topFive(String topFive) {
        Map map = new HashMap();
        Object object = redisUtil.get("flcxTopFive");

        List flcxRecordsList = null;
        if (null != object){
            flcxRecordsList = (List<Map<String, Object>>)object;
//            redisUtil.set("flcxTopFive", flcxRecordsList, 1);
        }else {
            String tableName = "flcx_records" + "_" + LocalDate.now().minusDays(1).toString().replace("-", "");
            //生成前一天查询量前五的数据放入缓存
            if(flcxRecordsMapper.existTable(tableName)<=0){
                //找不到前一天的数据
            }else {
                flcxRecordsList = flcxRecordsMapper.topFive(tableName);
                //放入缓存 12小时
                redisUtil.set("flcxTopFive", flcxRecordsList, 24*60*60-1);
            }
        }
        map.put("typeList", flcxRecordsList);
        return map;
    }

    @Override
    @Transactional
    public void saveFlcxRecords(FlcxRecords flcxRecords) {
        //分表保存入Mysql
        Date date=new Date();
        String tableName= ShardTableHelper.getTableNameByDay("flcx_records",date);
        if(flcxRecordsMapper.existTable(tableName)<=0){
            flcxRecordsMapper.createNewTable(tableName);
        }
        flcxRecordsMapper.insert(tableName,flcxRecords);
    }

    /** 查询所有词库中关键字搜索次数
      * @author sgmark@aliyun.com
      * @date 2019/7/3 0003
      * @param
      * @return
      */
    @Override
    public List<Map<String, Object>> selectRecordsCountList() {
        return flcxRecordsMapper.selectRecordsCountList(createTableNameByDay("flcx_records"));
    }
    /**
      * @author sgmark@aliyun.com
      * @date 2019/7/3 0003
      * @param 
      * @return 
      */
    public String  createTableNameByDay(String tableName){
        System.out.println( LocalDate.now().minusDays(1).toString().replace("-", ""));
        return  tableName + "_" + LocalDate.now().minusDays(1).toString().replace("-", "");
    }
}
