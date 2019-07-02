package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.entity.FlcxType;

import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxRecordsService extends IService<FlcxRecords> {
    Map topFive();

    void saveFlcxRecords(FlcxRecords flcxRecords);
}
