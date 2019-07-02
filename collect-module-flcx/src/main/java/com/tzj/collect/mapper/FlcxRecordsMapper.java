package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxRecords;
import org.apache.ibatis.annotations.Param;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxRecordsMapper extends BaseMapper<FlcxRecords> {
    int existTable(String tableName);
    int createNewTable(@Param("tableName")String tableName);
    int insert(@Param("tableName")String tableName,@Param("flcxRecords")FlcxRecords flcxRecords);
}
