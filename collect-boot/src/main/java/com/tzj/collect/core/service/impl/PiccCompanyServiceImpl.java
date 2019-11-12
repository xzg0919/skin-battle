package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.PiccCompanyMapper;
import com.tzj.collect.core.service.PiccCompanyService;
import com.tzj.collect.entity.PiccCompany;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PiccCompanyServiceImpl extends ServiceImpl<PiccCompanyMapper,PiccCompany> implements PiccCompanyService {


}
