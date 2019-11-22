package com.tzj.collect.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.service.FlcxCityService;
import com.tzj.collect.entity.FlcxCity;
import com.tzj.collect.mapper.FlcxCityMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
@Component
@Service(version = "${flcx.service.version}")
@Transactional(readOnly = true)
public class FlcxCityServiceImpl extends ServiceImpl<FlcxCityMapper, FlcxCity> implements FlcxCityService {
    @Resource
    private FlcxCityMapper flcxCityMapper;
    @Override
    public List getAllOpenCity(){
        return flcxCityMapper.getAllOpenCity();
    }
}
