package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.FlcxType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxTypeMapper extends BaseMapper<FlcxType> {
    List<Map<String, Object>> typeList(@Param("cityName")String city, @Param("cityId")String cityId);

    List<FlcxType> cityTypeList(@Param("cityId")String cityId);
}
