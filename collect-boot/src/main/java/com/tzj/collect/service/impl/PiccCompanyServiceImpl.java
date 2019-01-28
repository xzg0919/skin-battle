package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.PiccCompany;
import com.tzj.collect.mapper.PiccCompanyMapper;
import com.tzj.collect.service.PiccCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PiccCompanyServiceImpl extends ServiceImpl<PiccCompanyMapper,PiccCompany> implements PiccCompanyService {

    @Autowired
    private PiccCompanyMapper piccCompanyMapper;


}
