package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.PiccWaterMapper;
import com.tzj.collect.core.service.PiccWaterService;
import com.tzj.collect.entity.PiccWater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PiccWaterServiceImpl extends ServiceImpl<PiccWaterMapper,PiccWater> implements PiccWaterService {

    @Autowired
    private PiccWaterMapper piccWaterMapper;
}
