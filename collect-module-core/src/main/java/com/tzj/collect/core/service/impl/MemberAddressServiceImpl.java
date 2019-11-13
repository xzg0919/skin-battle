package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.utils.TableNameUtils;
import com.tzj.collect.core.mapper.MemberAddressMapper;
import com.tzj.collect.core.param.ali.MapAddressBean;
import com.tzj.collect.core.param.ali.MemberAddressBean;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.CommunityService;
import com.tzj.collect.core.service.MemberAddressService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.MemberAddress;
import com.tzj.collect.module.common.shard.ShardTableHelper;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会员地址ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class MemberAddressServiceImpl extends ServiceImpl<MemberAddressMapper, MemberAddress> implements MemberAddressService {
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
		MemberAddress memberAddress1 = this.getMemberAdderssByAliUserId(memberAddressBean.getAliUserId());
		if (null != memberAddress1){
			memberAddress1.setIsSelected(0);
			this.updateMemberAddressByAliUserId(memberAddress1);
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
	    		MemberAddress memberAddressc = new MemberAddress();
				memberAddressc.setId(Long.parseLong(memberAddressBean.getId()));
				memberAddressc.setAliUserId(memberAddressBean.getAliUserId());
				MemberAddress memberAddress = this.selectMemberAddressByAliUserIdOne(memberAddressc);
	    		memberAddress.setAliUserId(memberAddressBean.getAliUserId());
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
				this.updateMemberAddressByAliUserId(memberAddress);
	    	}else {
	    		MemberAddress memberAddress = new MemberAddress();
	    		memberAddress.setAliUserId(memberAddressBean.getAliUserId());
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
				this.insertMemberAddress(memberAddress);
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
	public List<MemberAddress> memberAddressList(String aliUserId) {
		MemberAddress memberAddress = new MemberAddress();
		memberAddress.setAliUserId(aliUserId);
		memberAddress.setDelFlag("0");
		return this.selectMemberAddressByAliUserId(memberAddress);
	}
	
	/**
	 * 用户删除地址时，如果是删除默认地址，自动将余下任意一条地址设置为默认地址
	 * @author wangcan
	 * @param memberAddressId : 地址id
	 * @return
	 */
	@Transactional
	@Override
	public String deleteByMemberId(String memberAddressId,String aliUserId) {
		//删除
		MemberAddress delete = new MemberAddress();
			delete.setId(Long.parseLong(memberAddressId));
			delete.setAliUserId(aliUserId);
		this.deleteMemberAddressByAliUserId(delete);
    	//查询用户的默认地址
    	MemberAddress memberAddress2 = this.getMemberAdderssByAliUserId(aliUserId);
    	if(memberAddress2 == null) {
    		//查询用户所有的余下地址
			MemberAddress selete = new MemberAddress();
				selete.setAliUserId(aliUserId);
				selete.setDelFlag("0");
			List<MemberAddress> MemberAddressList = this.selectMemberAddressByAliUserId(selete);
    		if(null!= MemberAddressList) {
    			MemberAddress memberAddresss = MemberAddressList.get(0);
    				memberAddresss.setIsSelected(1);
    			this.updateMemberAddressByAliUserId(memberAddresss);
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
		MemberAddress memberAddres = this.getMemberAdderssByAliUserId(memberAddressBean.getAliUserId());
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
				MemberAddress selete = new MemberAddress();
					selete.setId(Long.parseLong(memberAddressBean.getId()));
					selete.setAliUserId(memberAddressBean.getAliUserId());
				MemberAddress memberAddress = this.selectMemberAddressByAliUserIdOne(selete);
				memberAddress.setMemberId(memberAddressBean.getMemberId());
				memberAddress.setAliUserId(memberAddressBean.getAliUserId());
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
				this.updateMemberAddressByAliUserId(memberAddress);
			}else {
				if (null!=memberAddres){
					memberAddres.setIsSelected(0);
					this.updateMemberAddressByAliUserId(memberAddres);
				}
				MemberAddress memberAddress = new MemberAddress();
				memberAddress.setMemberId(memberAddressBean.getMemberId());
				memberAddress.setAliUserId(memberAddressBean.getAliUserId());
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
				this.insertMemberAddress(memberAddress);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return "保存地址失败";
		}
		return "保存地址成功";
	}

	@Transactional
	@Override
	public String saveMemberAddressdByMap(MapAddressBean mapAddressBean, String aliUserId) {
		System.out.println("用户开始新增地址了"+JSON.toJSONString(mapAddressBean));
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
			MemberAddress memberAddress1 = this.getMemberAdderssByAliUserId(aliUserId);
			if (null != memberAddress1){
				memberAddress1.setIsSelected(0);
				this.updateMemberAddressByAliUserId(memberAddress1);
			}
		}
		MemberAddress memberAddress = null;
		//判断是否是修改还是新增
		if(StringUtils.isNotBlank(mapAddressBean.getId())) {
			MemberAddress select = new MemberAddress();
				select.setId(Long.parseLong(mapAddressBean.getId()));
				select.setAliUserId(aliUserId);
			memberAddress = this.selectMemberAddressByAliUserIdOne(select);
			if (null == memberAddress){
				throw new ApiException("更改的地址不存在");
			}
		}else {
			memberAddress = new MemberAddress();
		}
		memberAddress.setMapName(mapAddressBean.getMapName());
		memberAddress.setMapAddress(mapAddressBean.getMapAddress());
		memberAddress.setAliUserId(aliUserId);
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
		this.inserOrUpdatetMemberAddress(memberAddress);
		return "操作成功";
	}

	@Override
	public MemberAddress getMemberAdderssByAliUserId(String aliUserId) {
		MemberAddress select = new MemberAddress();
			select.setDelFlag("0");
			select.setAliUserId(aliUserId);
			select.setIsSelected(1);
		List<MemberAddress> memberAddressesList = this.selectMemberAddressByAliUserId(select);
		MemberAddress memberAddress = null;
		if (null!=memberAddressesList&&!memberAddressesList.isEmpty()){
			memberAddress = memberAddressesList.get(0);
		}
		return memberAddress;
	}
	@Transactional
	@Override
	public Object updateIsSelectedAddress(String aliUserId,String id){
		MemberAddress memberAddress1 = this.getMemberAdderssByAliUserId(aliUserId);
		if(null != memberAddress1){
			memberAddress1.setIsSelected(0);
		}
		this.updateMemberAddressByAliUserId(memberAddress1);
		MemberAddress select = new MemberAddress();
			select.setAliUserId(aliUserId);
			select.setId(Long.parseLong(id));
		MemberAddress memberAddress = this.selectMemberAddressByAliUserIdOne(select);
		memberAddress.setIsSelected(1);
		this.updateMemberAddressByAliUserId(memberAddress);
		return "success";
	}

	@Override
	public String getMemberAddressById(String id,String aliUserId){
		MemberAddress select = new MemberAddress();
			select.setId(Long.parseLong(id));
			select.setAliUserId(aliUserId);
		MemberAddress memberAddress = this.selectMemberAddressByAliUserIdOne(select);
		String cityName = memberAddress.getCityName()==null?"":memberAddress.getCityName();
		if(org.apache.commons.lang3.StringUtils.isBlank(cityName)){
			Area city = areaService.selectById(memberAddress.getCityId());
			if(null != city){
				cityName = city.getAreaName();
			}
		}
		String areaName = memberAddress.getAreaName()==null?"":memberAddress.getAreaName();
		if(org.apache.commons.lang3.StringUtils.isBlank(areaName)){
			Area area = areaService.selectById(memberAddress.getAreaId());
			if(null != area){
				areaName = area.getAreaName();
			}
		}
		String streetName = memberAddress.getStreetName()==null?"":memberAddress.getStreetName();
		if(org.apache.commons.lang3.StringUtils.isBlank(streetName)){
			Area area = areaService.selectById(memberAddress.getStreetId());
			if(null != area){
				streetName = area.getAreaName();
			}
		}
		return cityName+areaName+streetName+memberAddress.getAddress()+memberAddress.getHouseNumber();
	}
	@Override
	public List<MemberAddress> selectMemberAddressByAliUserId(MemberAddress memberAddress) {
		return (List<MemberAddress>)this.selectMemberAddress(memberAddress,false);
	}
	@Override
	public MemberAddress selectMemberAddressByAliUserIdOne(MemberAddress memberAddress) {
		return (MemberAddress)this.selectMemberAddress(memberAddress,true);
	}

	@Override
	@Transactional
	public Integer deleteMemberAddressByAliUserId(MemberAddress memberAddress) {
		String memberAddressTableName = TableNameUtils.getMemberAddressTableName(memberAddress);
		memberAddress.setTableName(memberAddressTableName);
		return memberAddressMapper.deleteMemberAddressByAliUserId(memberAddress);
	}

	@Override
	@Transactional
	public Integer updateMemberAddressByAliUserId(MemberAddress memberAddress) {
		String memberAddressTableName = TableNameUtils.getMemberAddressTableName(memberAddress);
		memberAddress.setTableName(memberAddressTableName);
		return memberAddressMapper.updateMemberAddressByAliUserId(memberAddress);
	}

	@Override
	@Transactional
	public Integer insertMemberAddress(MemberAddress memberAddress) {
		String memberAddressTableName = TableNameUtils.getMemberAddressTableName(memberAddress);
		memberAddress.setTableName(memberAddressTableName);
		return memberAddressMapper.insertMemberAddress(memberAddress);
	}

	@Override
	@Transactional
	public Integer inserOrUpdatetMemberAddress(MemberAddress memberAddress) {
		if(null == memberAddress.getId()){
			return this.insertMemberAddress(memberAddress);
		}else {
			return this.updateMemberAddressByAliUserId(memberAddress);
		}
	}

	public Object selectMemberAddress(MemberAddress memberAddress,boolean isSelectOne) {
		String memberAddressTableName = TableNameUtils.getMemberAddressTableName(memberAddress);
		memberAddress.setTableName(memberAddressTableName);
		List<MemberAddress> memberAddressList = memberAddressMapper.selectMemberAddress(memberAddress);
		if (memberAddressList.isEmpty()){
			return null;
		}
		if (isSelectOne){
			return memberAddressList.get(0);
		}else {
			return memberAddressList;
		}
	}
}
