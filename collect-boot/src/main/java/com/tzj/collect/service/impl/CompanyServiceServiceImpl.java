package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.CompanyServiceRange;
import com.tzj.collect.mapper.CompanyServiceMapper;
import com.tzj.collect.service.CompanyServiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 回收企业服务范围ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class CompanyServiceServiceImpl extends ServiceImpl<CompanyServiceMapper, CompanyServiceRange> implements CompanyServiceService{
    
	@Autowired
	private CompanyServiceMapper companyServiceMapper;
	
	@Override
	public int selectCompanyServiceByCompanyId(long id) {
		
		return companyServiceMapper.selectCompanyServiceByCompanyId(id);
	}

	@Override
	public CompanyServiceRange selectRangeByCommunityId(Integer delectCommunityId) {
		EntityWrapper<CompanyServiceRange> wrapp = new EntityWrapper<CompanyServiceRange>();
		
		return selectOne(wrapp.eq("community_id", delectCommunityId));
	}

}
