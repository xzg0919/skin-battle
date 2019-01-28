package com.tzj.collect.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.result.BusinessRecType;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.mapper.CompanyMapper;
import com.tzj.collect.service.CompanyService;

@Service
@Transactional(readOnly=true)
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
	@Resource
	private CompanyMapper companyMapper;

	
	/**
	 * 返回企业信息列表
	 */
	@Override
	public Page<Company> getSearchCompany(CompanyBean companybean, PageBean pageBean) {
		
		Page<Company> page = new Page<Company>(pageBean.getPageNumber(), pageBean.getPageSize());
		EntityWrapper<Company> wrapper = new EntityWrapper<Company>();
		if(StringUtils.isNotBlank(companybean.getCompanyName())){			
			wrapper.eq("name_", companybean.getCompanyName());
		}
		
		return this.selectPage(page, wrapper);
	}
	
	@Override
	public Integer selectCompanyCountByCom(String comId) {
		return companyMapper.selectCompanyCountByCom(comId);
	}
	@Override
	public Integer selectCategoryCountByCom(String comId) {
		return companyMapper.selectCategoryCountByCom(comId);
	}

	@Override
	public List<Company> selectCompanyListByComm(String commId) {
		return companyMapper.selectCompanyListByComm(commId);
	}

	@Override
	public List<BusinessRecType> getTypeByComId(String comId) {
		return companyMapper.getTypeByComId(comId);
	}
	
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业Id
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	@Override
	public Integer getCompanyIdByIds(Integer CommunityId, Integer CategoryId) {
		
		return companyMapper.getCompanyIdByIds(CommunityId,CategoryId);
	}

	@Override
	public Company getCurrent(CompanyAccount companyAccount) {
		return this.selectById(companyAccount.getCompanyId());
	}
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	@Override
	public Company getCompanyByIds(Integer CommunityId, Integer CategoryId) {
		//私海没找到，找公海信息
		Company company = companyMapper.getCompanyByIds(CommunityId,CategoryId);
		if (company == null) {
			//若恒为null,根据小区 id找到所属区,再找到所属公海服务的公司
			company = companyMapper.getCompanyWithNotFound(CommunityId, CategoryId);
		}
		return company;
	}
}
