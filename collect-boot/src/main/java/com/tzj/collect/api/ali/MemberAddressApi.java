package com.tzj.collect.api.ali;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.MemberAddressBean;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.entity.*;
import com.tzj.collect.service.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 会员地址相关api
 * @Author 王美霞20180305
 **/
@ApiService
public class MemberAddressApi {
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private CompanyShareService companyShareService;
	@Autowired
	private LogisticsCompanyService logisticsCompanyService;

	/**
     * 保存用户的新增地址/修改后保存的地址
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.saveMemberAddress", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String saveMemberAddress(MemberAddressBean memberAddressBean) {
    	//获取当前登录的会员
		Member member = MemberUtils.getMember();
		memberAddressBean.setMemberId(member.getId().toString());
    	return memberAddressService.saveMemberAddress(memberAddressBean);
    }
    /**
     * 用户地址的列表
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.memberAddressList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<MemberAddress> memberAddressList(MemberAddressBean memberAddressBean) {
    	//获取当前登录的会员
		Member member = MemberUtils.getMember();
    	List<MemberAddress> memberAddressList = memberAddressService.memberAddressList(member.getId(),memberAddressBean.getCityId()+"");
    	return memberAddressList;
    }
    /**
     * 用户删除地址
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.delectMemberAddress", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String delectMemberAddress(MemberAddressBean memberAddressBean) {
    	/**
    	 * 用户删除地址时，如果是删除默认地址，自动将余下任意一条地址设置为默认地址
    	 */
    	//获取当前登录的会员
    	Member member = MemberUtils.getMember();
    	return memberAddressService.deleteByMemberId(memberAddressBean.getId(),member.getId());
    }
    /**
     * 根据地址Id查询具体地址
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.selectMemberAddress", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object selectMemberAddress(MemberAddressBean memberAddressBean) {
    	MemberAddress memberAddress = memberAddressService.selectById(memberAddressBean.getId());
    	Map<String,Object> map = new HashMap<String,Object>();
    	if(null!=memberAddress) {
    		//地址区的名称
    		String areaName = areaService.selectById(memberAddress.getAreaId()).getAreaName();
    		//地址街道名称
    		String streeName = areaService.selectById(memberAddress.getStreetId()).getAreaName();
			//地址街道名称
			String cityName = areaService.selectById(memberAddress.getCityId()).getAreaName();
    		//具体的某个小区
			String communityName = null;
			if(memberAddress.getCommunityId() == null  || "".equals(memberAddress.getCommunityId()) || "-1".equals(memberAddress.getCommunityId().toString())){
				communityName = memberAddress.getCommByUserInput();
			}else{
				communityName = communityService.selectById(memberAddress.getCommunityId()).getName();
			}
			map.put("cityName",cityName);
    		map.put("areaName",areaName);
    		map.put("streeName",streeName);
    		map.put("communityName",communityName);
    		map.put("memberAddress",memberAddress);
    		return map;
    	}else {
    		return "传入的Id查不到数据";
    	}
    }
    /**
     * 对外提供默认地址
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.memberAddress", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public MemberAddress memberAddress(MemberAddressBean memberAddressBean) {
    	//获取当前登录的会员
		Member member = MemberUtils.getMember();
		MemberAddress memberAddress = memberAddressService.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", member.getId()).eq("city_id", memberAddressBean.getCityId()));
		if(null==memberAddress) {
			memberAddress =new MemberAddress();
			memberAddress.setCommunityId(0);
			memberAddress.setAddress("请选择地址");
			memberAddress.setAreaId(0);
			memberAddress.setCreateDate(new Date());
			memberAddress.setHouseNumber("");
		}
		Integer communityId = memberAddress.getCommunityId();
		//根据小区Id查询小区信息
		Community community = communityService.selectById(communityId);
		//判断小区是否有定点回收
		if(community!=null&&StringUtils.isNotBlank(community.getFixedPointTime())&&StringUtils.isNotBlank(community.getFixedPointAddress())) {
			memberAddress.setIsFixedPoint("1");
		}else {
			memberAddress.setIsFixedPoint("0");
		}
		//判断地址是否有公司回收六废
		String companyId = selectCompanyId(25, memberAddress.getCommunityId(), memberAddress.getAreaId());
		if(StringUtils.isBlank(companyId)){
			memberAddress.setIsHousehold("N");
		}else{
			memberAddress.setIsHousehold("Y");
		}
		//判断该地址是否回收5公斤废纺衣物
		Integer logisticsId = logisticsCompanyService.selectLogisticeCompanyIds(45, memberAddress.getStreetId());
		if (null != logisticsId){
			memberAddress.setIsFiveKg("Y");
		}else{
			memberAddress.setIsFiveKg("N");
		}
		//判断地址是否有公司回收电器
		String companyIds = selectCompanyId(9, memberAddress.getCommunityId(), memberAddress.getAreaId());
		if(StringUtils.isBlank(companyIds)){
			memberAddress.setIsDigital("N");
		}else {
			memberAddress.setIsDigital("Y");
		}
		return  memberAddress;
    }
    /**
     * 根据地址id查询地址详情
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.getMemberAddressById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public MemberAddress getMemberAddressById(MemberAddressBean memberAddressBean) {
    	//获取当前登录的会员
    	Member member = MemberUtils.getMember();
    	MemberAddress memberAddress = null; 
		if(!StringUtils.isBlank(memberAddressBean.getId())) {
			memberAddress = memberAddressService.selectById(memberAddressBean.getId());
		}else {
			memberAddress = memberAddressService.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", member.getId()).eq("city_id", memberAddressBean.getCityId()));
		}
		
    	return  memberAddress;
    }
    /**
     *将地址设置为默认地址
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.updateIsSelectedAddress", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String updateIsSelectedAddress(MemberAddressBean memberAddressBean) {
    	//获取当前登录的会员
		Member member = MemberUtils.getMember();
		MemberAddress memberAddressk = new MemberAddress();
		memberAddressk.setIsSelected(0);
    	memberAddressService.update(memberAddressk, new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", member.getId()).eq("city_Id", memberAddressBean.getCityId()));
    	MemberAddress memberAddress = new MemberAddress();
    	memberAddress.setIsSelected(1);
    	memberAddressService.update(memberAddress, new EntityWrapper<MemberAddress>().eq("id",memberAddressBean.getId()).eq("del_flag", 0));
    	return "success";
    }

	/**
	 * 小程序保存用户的新增地址/修改后保存的地址
	 * @author 王灿
	 * @param
	 * @return
	 */
	@Api(name = "memberAddress.saveMemberAddressd", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public String saveMemberAddressd(MemberAddressBean memberAddressBean) {
		//获取当前登录的会员
		Member member = MemberUtils.getMember();
		memberAddressBean.setMemberId(member.getId().toString());
		return memberAddressService.saveMemberAddressd(memberAddressBean);
	}

	/**
	 * 根据小区Id,区域Id和分类Id查询所属企业
	 * @return
	 */
	public String selectCompanyId(Integer categoryId, Integer cummintyId, Integer areaId) {
		String companyId = "";
		//根据分类Id和小区Id查询所属企业
		Company companys = companyCategoryService.selectCompany(categoryId,cummintyId);
		if(companys == null) {
			//根据分类Id和小区id去公海查询相关企业
			CompanyShare companyShare =	companyShareService.selectOne(new EntityWrapper<CompanyShare>().eq("category_id", categoryId).eq("area_id", areaId));
			if(companyShare==null) {
				return companyId;
			}
			companyId = companyShare.getCompanyId().toString();
		}else {
			companyId = companys.getId().toString();
		}
		return companyId;
	}
    
}
