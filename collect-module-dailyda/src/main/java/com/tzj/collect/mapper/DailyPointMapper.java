package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.Point;
import com.tzj.collect.entity.PointList;
import org.apache.ibatis.annotations.Param;

public interface DailyPointMapper extends BaseMapper<Point>{

    void insertPointList(@Param("pointList")PointList pointList);
}
