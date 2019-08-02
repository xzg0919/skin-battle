package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.FlcxCity;
import com.tzj.collect.mapper.FlcxCityMapper;
import com.tzj.collect.service.FlcxCityService;
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
public class FlcxCityServiceImpl extends ServiceImpl<FlcxCityMapper, FlcxCity> implements FlcxCityService {

}
