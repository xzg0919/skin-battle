package com.tzj.point.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.JfappCompany;
import com.tzj.point.mapper.JfappCompanyMapper;
import com.tzj.collect.core.service.JfappCompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JfappCompanyServiceImpl extends ServiceImpl<JfappCompanyMapper, JfappCompany> implements JfappCompanyService {

}
