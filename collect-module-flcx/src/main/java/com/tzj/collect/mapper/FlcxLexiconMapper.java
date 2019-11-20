package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.result.flcx.FlcxResult;
import com.tzj.collect.entity.FlcxLexicon;
import org.apache.ibatis.annotations.Param;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxLexiconMapper extends BaseMapper<FlcxLexicon> {

    FlcxResult lexCheck(@Param("lexiconName") String lexiconName, @Param("typeId") Long typeId, @Param("cityName")String cityName, @Param("cityId")Long cityId);
    FlcxResult lexCheckSpecial(@Param("lexiconName") String lexiconName, @Param("typeId") Long typeId, @Param("cityName")String cityName, @Param("cityId")Long cityId);
}
