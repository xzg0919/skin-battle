package com.tzj.collect.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.DailyLexicon;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
  * @author sgmark@aliyun.com
  * @date 2019/8/15 0015
  * @param 
  * @return 
  */
public interface DailyLexiconMapper extends BaseMapper<DailyLexicon> {
    /** 所有的题库(id, name, ,typeId, depth)
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    List<Map<String, Object>> lexiconList();
    /** 答题开始新增记录
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    void insertDailyRecords(@Param("aliUserId") String aliUserId, @Param("uuId") String uuId, @Param("lexiconId") Long lexiconId, @Param("typeId") Integer typeId, @Param("tableName")String tableName);

    Integer updateDailyRecordsList(@Param("aliUserId")String aliUserId , @Param("tableName")String tableName, @Param("uuId")String uuId, @Param("typeId")Serializable typeId, @Param("trueOrFalse")String trueOrFalse, @Param("localDateBefore")String localDateBefore, @Param("localDateEnd")String localDateEnd);

    Map<String, Object> checkDailyIsAnswer(@Param("uuId")String uuId, @Param("tableName")String tableName);

    Set<Map<String, Object>> dailyLexiconList(@Param("aliUserId")String aliUserId, @Param("tableName")String tableName, @Param("localDateBefore")String localDateBefore, @Param("localDateEnd")String localDateEnd);

    List<Map<String, Object>> errorLexiconList(@Param("tableName")String tableName, @Param("aliUserId")String aliUserId, @Param("localDateBefore")String localDateBefore, @Param("localDateEnd")String localDateEnd);

    Set<Map<String, Object>> isAnswerDaily(@Param("aliUserId")String aliUserId, @Param("tableName")String tableName, @Param("localDateBefore")String localDateBefore, @Param("localDateEnd")String localDateEnd);

    Integer existTable(@Param("tableName")String tableName);

    void createNewTable(@Param("tableName")String tableName);

    List<Map<String, Object>> lexiconListByAliUserId(@Param("aliUserId")String aliUserId, @Param("tableName")String tableName, @Param("localDateBefore")String localDateBefore, @Param("localDateEnd")String localDateEnd);

//    Map<String, Object> selectCountAndPrices(@Param("tableName")String tableName, @Param("aliUserId")String aliUserId, @Param("localDateBefore")String localDateBefore, @Param("localDateEnd")String localDateEnd);

    Map<String, Object> selectSums(@Param("tableName")String tableName, @Param("aliUserId")String aliUserId);

    Map<String, Object> selectPrices(@Param("aliUserId")String aliUserId, @Param("localDateBefore")String localDateBefore, @Param("localDateEnd")String localDateEnd);

    Map<String, Object> selectPricesSumsAndMemInfoByAliId(@Param("aliUserId")String aliUserId, @Param("tableNameLastWeek")String tableNameLastWeek, @Param("yearWeek")String yearWeek, @Param("tableName")String tableName);

    void deleteDailyRecords(@Param("aliUserId")String aliUserId, @Param("tableName")String tableName, @Param("todayTime")String todayTime);

    Map<String, Object> selectYesterdayNumber(@Param("tableName") String tableName, @Param("yesterday") String yesterday);
}
