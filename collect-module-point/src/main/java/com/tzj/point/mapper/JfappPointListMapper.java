package com.tzj.point.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.JfappPointList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface JfappPointListMapper extends BaseMapper<JfappPointList> {

    List<Map<String,Object>> getJfappPointList(@Param("startSize") Integer startSize,@Param("pageSize") Integer pageSize, @Param("jfappRecyclerId") Long jfappRecyclerId);

    List<Map<String,Object>> getJfPointListByAdmin(@Param("userName")String userName,@Param("recyclerName")String recyclerName,@Param("mobile")String mobile,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("startSize")Integer startSize,@Param("pageSize")Integer pageSize);

    Integer getJfPointListCountByAdmin(@Param("userName")String userName,@Param("recyclerName")String recyclerName,@Param("mobile")String mobile,@Param("startDate")String startDate,@Param("endDate")String endDate);

}
