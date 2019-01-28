package com.tzj.collect.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.CompanyShare;
import com.tzj.collect.mapper.CompanyShareMapper;
import com.tzj.collect.service.CompanyShareService;


@Service
@Transactional(readOnly=true)
public class CompanyShareServiceImpl  extends  ServiceImpl< CompanyShareMapper, CompanyShare> implements CompanyShareService{
	@Autowired
	private CompanyShareMapper companyShareMapper;
}
