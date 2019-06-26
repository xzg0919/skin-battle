package com.tzj.collect.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tzj.collect.entity.CompanyShare;
import com.tzj.collect.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
@Transactional(readOnly=true)
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
	@Resource
	private CompanyMapper companyMapper;
	@Autowired
	private RecyclersRangeApplianceService recyclersRangeApplianceService;
	@Autowired
	private RecyclersRangeHouseholdService recyclersRangeHouseholdService;
	@Autowired
	private RecyclersRangeBigService recyclersRangeBigService;
	@Autowired
	private CompanyServiceService companyServiceService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;
	@Autowired
	private CompanyStreetApplianceService companyStreetApplianceService;

	
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

	@Override
	public String selectIotUrlByEquipmentCode(String cabinetNo) {

		return companyMapper.selectIotUrlByEquipmentCode(cabinetNo);
	}

	@Transactional
	@Override
	public String isOpenOrder(String isOpenOrder, Integer companyId) {

		Company company = this.selectById(companyId);
		company.setIsOpenOrder(isOpenOrder);
		this.updateById(company);
		return "操作成功";
	}
	@Override
	public  Object companyAreaRanges(String title,String companyId){
		Map<String,Object> resutMap = new HashMap<>();
		Map<String,Object> companyRange = new HashMap<>();
		Map<String,Object> companyRecycleRange = new HashMap<>();
		if ("1".equals(title)){
			companyRange = companyStreetApplianceService.companyAreaRanges(companyId);
			companyRecycleRange = recyclersRangeApplianceService.companyAreaRecyclerRanges(companyId);
		}else if("2".equals(title)){
			companyRange = companyServiceService.companyAreaRanges(companyId);
			companyRecycleRange = recyclersRangeHouseholdService.companyAreaRecyclerRanges(companyId);
		}else if("4".equals(title)){
			companyRange = companyStreetBigService.companyAreaRanges(companyId);
			companyRecycleRange = recyclersRangeBigService.companyAreaRecyclerRanges(companyId);
		}
		if(companyRange.isEmpty()){
			companyRange.put("cityNum",0);
			companyRange.put("areaNum",0);
			companyRange.put("streetNum",0);
			companyRange.put("communityNum",0);
		}
		if(companyRecycleRange.isEmpty()){
			companyRecycleRange.put("cityNum",0);
			companyRecycleRange.put("areaNum",0);
			companyRecycleRange.put("streetNum",0);
			companyRecycleRange.put("communityNum",0);
		}
		resutMap.put("companyRange",companyRange);
		resutMap.put("companyRecycleRange",companyRecycleRange);
		return resutMap;
	}
}
