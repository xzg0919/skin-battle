package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.mapper.CommunityMapper;
import com.tzj.collect.service.CommunityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class CommunityServiceImpl  extends ServiceImpl<CommunityMapper, Community> implements CommunityService{

	@Resource
	private CommunityMapper communityMapper;
	
	@Override
	public List<Community> areaCommunity(Integer areaId) {
		return selectList(new EntityWrapper<Community>().eq("area_id", areaId).eq("del_flag", "0").orderBy("initials", true));
	}
	
	/**
	 * 根据街道id获取小区列表并根据用户的经纬度径行由近到远的排序
	 * @author wangcan
	 * @param areaId : 街道Id
	 * @param longitude : 经度
	 * @param latitude : 纬度
	 */
	@Override
	public List<Community> areaCommunityList(Integer areaId,double longitude,double latitude){
		
		return communityMapper.areaCommunityList(areaId,longitude,latitude);
	}

	@Override
	public List<Community> listareaByCategory(Integer categoryId,Integer areaId) {
		return communityMapper.listareaByCategory(categoryId,areaId);
	}

	@Override
	public Community defaultAddress(Integer communityId,Integer categoryId) {
		return communityMapper.defaultAddress(communityId, categoryId);
 	}


	@Override
	public List<Recyclers> getRecByCommId(String commId) {
		return communityMapper.getRecByCommId(commId);
	}


	
	
	@Override
	public List<AdminCommunityBean> selectCommunityByConditions(CompanyBean companybean) {
		
		return communityMapper.selectCommunityByConditions(companybean);
	}

	@Override
	public List<AdminCommunityBean> getSelectedCommunityListByCompanyId(long id) {
		
		return communityMapper.getSelectedCommunityListByCompanyId(id);
	}
/**
 * 根据公司id和区县或者街道id显示已过滤的小区(点击 编辑后显示的页面----只传区县或者街道id)
 */
	@Override
	public List<AdminCommunityBean> getEditorCommunity(CompanyBean companybean) {
		
		return communityMapper.getEditorCommunity(companybean);
	}
	/**
     * 根据企业id得到回收人员信息
     * @param companyId : 企业id
     * @return
     */
	@Override
	public List<Area> getCommunityData(String companyId) {
		 List<Area> list = communityMapper.getCommunityData(companyId);
		return list;
	}

}
