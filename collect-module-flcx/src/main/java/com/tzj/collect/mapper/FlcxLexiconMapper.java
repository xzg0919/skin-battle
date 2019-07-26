package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.api.lexicon.result.FlcxResult;
import com.tzj.collect.entity.FlcxLexicon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxLexiconMapper extends BaseMapper<FlcxLexicon> {
    FlcxResult lexCheck(@Param("lexiconName") String lexiconName, @Param("typeId") Long typeId, @Param("cityName")String cityName, @Param("cityId")Long cityId);
    List<Map<String, String>> lexCheckCount(@Param("lexiconName") String lexiconName);
}
