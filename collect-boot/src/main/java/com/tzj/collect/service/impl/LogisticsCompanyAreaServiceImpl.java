package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.LogisticsCompanyArea;
import com.tzj.collect.mapper.LogisticsCompanyAreaMapper;
import com.tzj.collect.service.LogisticsCompanyAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class LogisticsCompanyAreaServiceImpl extends ServiceImpl<LogisticsCompanyAreaMapper,LogisticsCompanyArea> implements LogisticsCompanyAreaService {

    @Autowired
    private LogisticsCompanyAreaMapper logisticsCompanyAreaMapper;




}
