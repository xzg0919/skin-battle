package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.entity.FlcxType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxRecordsService extends IService<FlcxRecords> {
    Map topFive(String topFive);

    @Transactional
    void saveFlcxRecords(FlcxRecords flcxRecords);

    List<Map<String, Object>> selectRecordsCountList();
}
