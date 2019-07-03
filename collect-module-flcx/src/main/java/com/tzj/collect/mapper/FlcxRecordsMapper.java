package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxRecords;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxRecordsMapper extends BaseMapper<FlcxRecords> {
    int existTable(@Param("tableName") String tableName);
    int createNewTable(@Param("tableName") String tableName);
    int insert(@Param("tableName") String tableName,@Param("flcxRecords") FlcxRecords flcxRecords);
    List<Map<String, Object>> topFive(@Param("tableName")String tableName);

    List<Map<String, Object>> selectRecordsCountList(@Param("tableName")String flcx_records);
}
