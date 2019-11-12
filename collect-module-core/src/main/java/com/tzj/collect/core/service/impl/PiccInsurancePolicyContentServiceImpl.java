package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.PiccInsurancePolicyContentMapper;
import com.tzj.collect.core.service.PiccInsurancePolicyContentService;
import com.tzj.collect.entity.PiccInsurancePolicyContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PiccInsurancePolicyContentServiceImpl extends ServiceImpl<PiccInsurancePolicyContentMapper,PiccInsurancePolicyContent> implements PiccInsurancePolicyContentService {

    @Autowired
    private PiccInsurancePolicyContentMapper piccInsurancePolicyContentMapper;
}
