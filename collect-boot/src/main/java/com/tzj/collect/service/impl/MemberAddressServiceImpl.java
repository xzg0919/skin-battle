package com.tzj.collect.service.impl;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tzj.collect.api.ali.param.MapAddressBean;
import com.tzj.collect.entity.Community;
import com.tzj.collect.service.CommunityService;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.ali.param.MemberAddressBean;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.MemberAddress;
import com.tzj.collect.mapper.CompanyServiceMapper;
import com.tzj.collect.mapper.MemberAddressMapper;
import com.tzj.collect.service.AreaService;
import com.tzj.collect.service.MemberAddressService;

import javax.annotation.Resource;

/**
 * 会员地址ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class MemberAddressServiceImpl extends ServiceImpl<MemberAddressMapper, MemberAddress> implements MemberAddressService{
	@Autowired
	private MemberAddressMapper memberAddressMapper;
	@Autowired
	private AreaService areaService;
	@Autowired
	private CommunityService communityService;
	
	 /**
     * 保存用户的地址
     * @author 王灿
     * @param memberAddressBean
     * @return
     */ 
    @Transactional(readOnly=false)
	public String saveMemberAddress(MemberAddressBean memberAddressBean) {
		MemberAddress memberAddress1 = this.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected", 1).eq("del_flag", 0).eq("member_id", memberAddressBean.getMemberId()).eq("city_id", memberAddressBean.getCityId()));
		if (null != memberAddress1){
			memberAddress1.setIsSelected(0);
			this.updateById(memberAddress1);
		}
    	try {
    		if(StringUtils.isBlank(memberAddressBean.getName())) {
    			return "请输入名字";
    		}
    		if(StringUtils.isBlank(memberAddressBean.getTel())) {
    			return "请输入手机号码";
    		}
    		if(StringUtils.isBlank(memberAddressBean.getAddress())) {
    			return "地址不能为空";
    		}
    		if(StringUtils.isBlank(memberAddressBean.getHouseNumber())) {
    			return "具体的门牌号不能为空";
    		}
    		if (StringUtils.isBlank(memberAddressBean.getCommunityId()) && StringUtils.isBlank(memberAddressBean.getCommByUserInput())){
				return "小区不能为空";
			}

			Area area1 = areaService.selectById(memberAddressBean.getAreaId());
			Area street = areaService.selectById(memberAddressBean.getStreetId());
			Community community = communityService.selectById(memberAddressBean.getCommunityId());
			//判断是否是修改还是新增
	    	if(StringUtils.isNotBlank(memberAddressBean.getId())) {
	    		MemberAddress memberAddress = this.selectById(memberAddressBean.getId());
	    		memberAddress.setMemberId(memberAddressBean.getMemberId());
	    		memberAddress.setName(memberAddressBean.getName());
	    		memberAddress.setAreaId(Integer.parseInt(memberAddressBean.getAreaId()));
	    		memberAddress.setStreetId(Integer.parseInt(memberAddressBean.getStreetId()));
				memberAddress.setCommByUserInput(memberAddressBean.getCommByUserInput());
				memberAddress.setCommunityId(StringUtils.isBlank(memberAddressBean.getCommunityId())?-1:Integer.parseInt(memberAddressBean.getCommunityId()));
				//根据市级Id获取市级信息
				Area area = areaService.selectById(memberAddressBean.getCityId());
	    		memberAddress.setAddress(area.getAreaName()+memberAddressBean.getAddress());
	    		memberAddress.setHouseNumber(memberAddressBean.getHouseNumber());
	    		memberAddress.setTel(memberAddressBean.getTel());
	    		memberAddress.setIsSelected(1);
				memberAddress.setCityId(memberAddressBean.getCityId());
				memberAddress.setCityName(area.getAreaName());
				memberAddress.setAreaName(area1.getAreaName());
				memberAddress.setStreetName(street.getAreaName());
				if(null != community){
					memberAddress.setCommunityName(community.getName());
				}
	    		this.updateById(memberAddress);
	    	}else {
	    		MemberAddress memberAddress = new MemberAddress();
	    		memberAddress.setMemberId(memberAddressBean.getMemberId());
	    		memberAddress.setName(memberAddressBean.getName());
	    		memberAddress.setAreaId(Integer.parseInt(memberAddressBean.getAreaId()));
	    		memberAddress.setStreetId(Integer.parseInt(memberAddressBean.getStreetId()));
				memberAddress.setCommunityId(StringUtils.isBlank(memberAddressBean.getCommunityId())?-1:Integer.parseInt(memberAddressBean.getCommunityId()));
				memberAddress.setCommByUserInput(memberAddressBean.getCommByUserInput());
				//根据市级Id获取市级信息
				Area area = areaService.selectById(memberAddressBean.getCityId());
				String areaName = "";
				if(area==null) {
					areaName = memberAddressBean.getAddress();
				}else {
					areaName = area.getAreaName()+memberAddressBean.getAddress();
				}
	    		memberAddress.setAddress(areaName);
	    		memberAddress.setHouseNumber(memberAddressBean.getHouseNumber());
	    		memberAddress.setTel(memberAddressBean.getTel());
	    		memberAddress.setIsSelected(1);
	    		memberAddress.setCityId(memberAddressBean.getCityId());
				memberAddress.setCityName(area.getAreaName());
				memberAddress.setAreaName(area1.getAreaName());
				memberAddress.setStreetName(street.getAreaName());
				if(null != community){
					memberAddress.setCommunityName(community.getName());
				}
	    		this.insert(memberAddress);
	    	}
    	}catch(Exception e) {
    		e.printStackTrace();
    		return "保存地址失败";
    	}
		return "保存地址成功";
	}
    
    /**
     * 用户地址的列表
     * @author 王灿
     * @param 
     * @return 
     */
	@Override
	public List<MemberAddress> memberAddressList(long memberId,String cityId) {
		return this.selectList(new EntityWrapper<MemberAddress>().eq("del_flag", 0).eq("member_id", memberId).eq("city_id", cityId).orderBy("is_selected", false));
	}
	
	/**
	 * 用户删除地址时，如果是删除默认地址，自动将余下任意一条地址设置为默认地址
	 * @author wangcan
	 * @param memberAddressId : 地址id
	 * @return
	 */
	@Transactional
	@Override
	public String deleteByMemberId(String memberAddressId,long memberId) {
		//根据地址Id查询地址信息
		MemberAddress memberAddress =this.selectById(memberAddressId);
    	memberAddress.setDelFlag("1");
    	this.updateById(memberAddress);
    	Integer cityId = memberAddress.getCityId();
    	//查询用户的默认地址
    	MemberAddress memberAddress2 = this.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", memberId).eq("city_id",cityId));
    	if(memberAddress2 == null) {
    		//查询用户所有的余下地址
    		List<MemberAddress> MemberAddressList = this.selectList(new EntityWrapper<MemberAddress>().eq("del_flag", 0).eq("member_id", memberId).eq("city_id", cityId).orderBy("create_date", false));
    		if(!MemberAddressList.isEmpty()) {
    			MemberAddress memberAddresss = MemberAddressList.get(0);
    			memberAddresss.setIsSelected(1);
    			this.updateById(memberAddresss);
    		}
    	}
		return "success";
	}

	/**
	 * 小程序保存用户的地址
	 * @author 王灿
	 * @param memberAddressBean
	 * @return
	 */
	@Transactional(readOnly=false)
	public String saveMemberAddressd(MemberAddressBean memberAddressBean) {
		String isSelected = memberAddressBean.getIsSelected();
		//判断是否将地址设置为默认
		MemberAddress memberAddres = this.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected", 1).eq("del_flag", 0).eq("member_id", memberAddressBean.getMemberId()).eq("city_id", memberAddressBean.getCityId()));
		if(memberAddres==null){
			isSelected = "1";
		}else{
			if(memberAddres.getId().toString().equals(memberAddressBean.getId())){
				isSelected = "1";
			}else{
				isSelected = "0";
			}
		}

		try {
			//根据行政区姓名查询行政区信息
			Area areas = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", memberAddressBean.getAreaName()).eq("parent_id",memberAddressBean.getCityId()));
			//根据街道姓名查询街道信息
			Area street = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", memberAddressBean.getStreetName()).eq("parent_id",areas.getId()));
			//根据小区姓名查询小区信息
			Community community = communityService.selectOne(new EntityWrapper<Community>().eq("name_",memberAddressBean.getCommunityName()).eq("area_id",street.getId()));

			String adreess = memberAddressBean.getAreaName()+memberAddressBean.getStreetName()+memberAddressBean.getCommunityName()+(StringUtils.isBlank(memberAddressBean.getCommByUserInput())?"":memberAddressBean.getCommByUserInput())+memberAddressBean.getHouseNumber();

			//判断是否是修改还是新增
			if(StringUtils.isNotBlank(memberAddressBean.getId())) {
				MemberAddress memberAddress = this.selectById(memberAddressBean.getId());
				memberAddress.setMemberId(memberAddressBean.getMemberId());
				memberAddress.setName(memberAddressBean.getName());
				memberAddress.setTel(memberAddressBean.getTel());
				memberAddress.setAreaId(Integer.parseInt(areas.getId().toString()));
				memberAddress.setStreetId(Integer.parseInt(street.getId().toString()));
				memberAddress.setCommunityId(community==null?-1:Integer.parseInt(community.getId().toString()));
				memberAddress.setCommByUserInput(StringUtils.isBlank(memberAddressBean.getCommByUserInput())?"":memberAddressBean.getCommByUserInput());
				//根据市级Id获取市级信息
				Area area = areaService.selectById(memberAddressBean.getCityId());
				memberAddress.setAddress(area.getAreaName()+adreess);
				memberAddress.setHouseNumber(memberAddressBean.getHouseNumber());
				memberAddress.setIsSelected(Integer.parseInt(isSelected));
				memberAddress.setCityId(memberAddressBean.getCityId());
				this.updateById(memberAddress);
			}else {
				if (null!=memberAddres){
					memberAddres.setIsSelected(0);
					this.updateById(memberAddres);
				}
				MemberAddress memberAddress = new MemberAddress();
				memberAddress.setMemberId(memberAddressBean.getMemberId());
				memberAddress.setName(memberAddressBean.getName());
				memberAddress.setTel(memberAddressBean.getTel());
				memberAddress.setAreaId(Integer.parseInt(areas.getId().toString()));
				memberAddress.setStreetId(Integer.parseInt(street.getId().toString()));
				memberAddress.setCommunityId(community==null?-1:Integer.parseInt(community.getId().toString()));
				memberAddress.setCommByUserInput(StringUtils.isBlank(memberAddressBean.getCommByUserInput())?"":memberAddressBean.getCommByUserInput());
				//根据市级Id获取市级信息
				Area area = areaService.selectById(memberAddressBean.getCityId());
				String areaName = "";
				if(area==null) {
					areaName = adreess;
				}else {
					areaName = area.getAreaName()+adreess;
				}
				memberAddress.setAddress(areaName);
				memberAddress.setHouseNumber(memberAddressBean.getHouseNumber());
				memberAddress.setIsSelected(1);
				memberAddress.setCityId(memberAddressBean.getCityId());
				this.insert(memberAddress);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return "保存地址失败";
		}
		return "保存地址成功";
	}

	@Transactional
	@Override
	public String saveMemberAddressdByMap(MapAddressBean mapAddressBean, long memberId) {
		Integer cityId = -1;
		Integer areaId = -1;
		Integer streetId = -1;
		Integer communityId = -1;
		Area townShip = areaService.selectOne(new EntityWrapper<Area>().eq("code_", mapAddressBean.getTownCode()));
		if(null!=townShip){
			streetId = townShip.getId().intValue();
			Area area = areaService.selectById(townShip.getParentId());
			cityId = area.getParentId();
			areaId = area.getId().intValue();
		}else {
			Area city = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", mapAddressBean.getCity()).eq("type", 1));
			if(null!=city){
				cityId = city.getId().intValue();
			}
			Area district = areaService.selectOne(new EntityWrapper<Area>().eq("parent_id", cityId).eq("area_name", mapAddressBean.getDistrict()).eq("type", 2));
			if(null!=district){
				areaId = district.getId().intValue();
			}
		}
		Community community = communityService.selectOne(new EntityWrapper<Community>().eq("area_id", streetId).eq("name_", mapAddressBean.getName()));
		if(null!=community){
			communityId = community.getId().intValue();
		}
		if("1".equals(mapAddressBean.getIsSelected())){
			MemberAddress memberAddress1 = this.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected", 1).eq("del_flag", 0).eq("member_id", memberId).eq("city_id",cityId));
			if (null != memberAddress1){
				memberAddress1.setIsSelected(0);
				this.updateById(memberAddress1);
			}
		}
		MemberAddress memberAddress = null;
		//判断是否是修改还是新增
		if(StringUtils.isNotBlank(mapAddressBean.getId())) {
			memberAddress = this.selectById(mapAddressBean.getId());
			if (null == memberAddress){
				throw new ApiException("更改的地址不存在");
			}
		}else {
			memberAddress = new MemberAddress();
		}
		memberAddress.setMemberId(memberId+"");
		memberAddress.setName(mapAddressBean.getUserName());
		memberAddress.setTel(mapAddressBean.getTel());
		memberAddress.setCityId(cityId);
		memberAddress.setAreaId(areaId);
		memberAddress.setStreetId(streetId);
		memberAddress.setCommunityId(communityId);
		memberAddress.setAddress(mapAddressBean.getAddress());
		memberAddress.setHouseNumber(mapAddressBean.getHouseNumber());
		memberAddress.setIsSelected(Integer.parseInt(mapAddressBean.getIsSelected()));
		memberAddress.setCityName(mapAddressBean.getCity());
		memberAddress.setAreaName(mapAddressBean.getDistrict());
		memberAddress.setStreetName(mapAddressBean.getTownShip());
		memberAddress.setCommunityName(mapAddressBean.getName());
		memberAddress.setTownCode(mapAddressBean.getTownCode());
		this.insertOrUpdate(memberAddress);

		return "操作成功";
	}
}
