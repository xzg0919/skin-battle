package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.entity.FlcxType;
import com.tzj.collect.mapper.FlcxRecordsMapper;
import com.tzj.collect.mapper.FlcxTypeMapper;
import com.tzj.collect.service.FlcxRecordsService;
import com.tzj.collect.service.FlcxTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
@Service
@Transactional(readOnly = true)
public class FlcxRecordsServiceImpl extends ServiceImpl<FlcxRecordsMapper, FlcxRecords> implements FlcxRecordsService {

}
