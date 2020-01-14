package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.Area;

import java.util.List;
import java.util.Map;

public interface AreaMapper extends BaseMapper<Area> {

    List<Map<String,Object>>  getCityList();


}
