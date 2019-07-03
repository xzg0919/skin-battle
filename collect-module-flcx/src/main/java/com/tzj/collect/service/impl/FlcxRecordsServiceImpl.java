package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.entity.FlcxType;
import com.tzj.collect.mapper.FlcxRecordsMapper;
import com.tzj.collect.mapper.FlcxTypeMapper;
import com.tzj.collect.module.common.shard.ShardTableHelper;
import com.tzj.collect.service.FlcxRecordsService;
import com.tzj.collect.service.FlcxTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Override
    public Map topFive() {
       return null;
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
}
