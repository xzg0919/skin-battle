package com.tzj.collect.core.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.DailyLexicon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
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
}
