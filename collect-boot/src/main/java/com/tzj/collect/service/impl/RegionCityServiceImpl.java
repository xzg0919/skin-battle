package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.RegionCity;
import com.tzj.collect.mapper.RegionCityMapper;
import com.tzj.collect.service.RegionCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class RegionCityServiceImpl extends ServiceImpl<RegionCityMapper,RegionCity>  implements RegionCityService{

    @Autowired
    private RegionCityMapper regionCityMapper;


}
