package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.FlcxCity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxCityMapper extends BaseMapper<FlcxCity> {
    //提供单位联合发布
    Map<String, Object> synPro(@Param("cityName") String cityName, @Param("cityId") String cityId);

    //获取所有的开通城市
    List getAllOpenCity();
}
