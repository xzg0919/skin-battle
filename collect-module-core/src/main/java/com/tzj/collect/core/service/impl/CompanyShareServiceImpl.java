package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyShareMapper;
import com.tzj.collect.core.service.CompanyShareService;
import com.tzj.collect.entity.CompanyShare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class CompanyShareServiceImpl  extends  ServiceImpl<CompanyShareMapper, CompanyShare> implements CompanyShareService {
	@Autowired
	private CompanyShareMapper companyShareMapper;
}
