package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.FlcxType;

import java.util.List;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxTypeService extends IService<FlcxType> {
    @DS("slave")
    Map typeList();

    void inputLinAndType(List<Map<String, String>> mapList);
}
