package com.tzj.collect.api.ali;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.MapAddressBean;
import com.tzj.collect.core.param.ali.MemberAddressBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.MemberAddress;
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
	private CompanyStreeService companyStreeService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;
	@Autowired
	private CompanyStreetApplianceService companyStreetApplianceService;
	@Autowired
	private CompanyStreetHouseService companyStreetHouseService;

	/**
     * 保存用户的新增地址/修改后保存的地址
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.saveMemberAddress", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String saveMemberAddress(MemberAddressBean memberAddressBean) {
    	//获取当前登录的会员
		Member member = MemberUtils.getMember();
		memberAddressBean.setAliUserId(member.getAliUserId());
    	return memberAddressService.saveMemberAddress(memberAddressBean);
    }
    /**
     * 用户地址的列表
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.memberAddressList", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<MemberAddress> memberAddressList(MemberAddressBean memberAddressBean) {
    	//获取当前登录的会员
		Member member = MemberUtils.getMember();
    	List<MemberAddress> memberAddressList = memberAddressService.memberAddressList(member.getAliUserId());
    	return memberAddressList;
    }
    /**
     * 用户删除地址
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.delectMemberAddress", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String delectMemberAddress(MemberAddressBean memberAddressBean) {
    	/**
    	 * 用户删除地址时，如果是删除默认地址，自动将余下任意一条地址设置为默认地址
    	 */
    	//获取当前登录的会员
    	Member member = MemberUtils.getMember();
    	return memberAddressService.deleteByMemberId(memberAddressBean.getId(),member.getAliUserId());
    }
    /**
     * 根据地址Id查询具体地址
     * @author 王灿
     * @param 
     * @return 
     */
    @Api(name = "memberAddress.selectMemberAddress", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	@DS("slave")
    public Object selectMemberAddress(MemberAddressBean memberAddressBean) {
		Member member = MemberUtils.getMember();
		MemberAddress select = new MemberAddress();
			select.setId(Long.parseLong(memberAddressBean.getId()));
			select.setAliUserId(member.getAliUserId());
		MemberAddress memberAddress = memberAddressService.selectMemberAddressByAliUserIdOne(select);
    	Map<String,Object> map = new HashMap<String,Object>();
		String areaName = "";
		String streeName ="";
		String cityName = "";
    	if(null!=memberAddress) {
    		//地址区的名称
			Area area = areaService.selectById(memberAddress.getAreaId());
			if(null != area ){
				areaName = area.getAreaName();
			}else {
				areaName = memberAddress.getAreaName();
			}
    		//地址街道名称
			Area street = areaService.selectById(memberAddress.getStreetId());
			if(null != street ){
				streeName = street.getAreaName();
			}else {
				streeName = memberAddress.getStreetName();
			}
			//地址市名称
			Area city = areaService.selectById(memberAddress.getCityId());
			if(null != city ){
				cityName = city.getAreaName();
			}else {
				cityName = memberAddress.getCityName();
			}
    		//具体的某个小区
			String communityName = "";
			if(memberAddress.getCommunityId() == null  || "".equals(memberAddress.getCommunityId()) || "-1".equals(memberAddress.getCommunityId().toString())){
				communityName = memberAddress.getCommByUserInput();
				if (StringUtils.isBlank(memberAddress.getCommByUserInput())){
					communityName = memberAddress.getCommunityName();
				}
			}else{
				communityName = communityService.selectById(memberAddress.getCommunityId()).getName();
			}
			map.put("mapName",memberAddress.getMapName());
			map.put("mapAddress",memberAddress.getMapAddress());
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
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public MemberAddress memberAddress(MemberAddressBean memberAddressBean) {
    	//获取当前登录的会员
		Member member = MemberUtils.getMember();
		if(member==null){
			return  null;
		}
		MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
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
		//根据分类Id和小区Id查询所属企业
		String companyId = companyStreetHouseService.selectStreetHouseceCompanyId(memberAddress.getStreetId(), memberAddress.getCommunityId());
		//Company companys = companyCategoryService.selectCompany(25,communityId);
		if(!StringUtils.isBlank(companyId)){
			memberAddress.setIsHousehold("Y");
		}else{
			memberAddress.setIsHousehold("N");
		}
		//判断该地址是否回收5公斤废纺衣物
		Integer streeCompanyId = companyStreeService.selectStreeCompanyIds(45, memberAddress.getStreetId());
		if (null != streeCompanyId){
			memberAddress.setIsFiveKg("Y");
		}else{
			memberAddress.setIsFiveKg("N");
		}
		//判断地址是否有公司回收电器
		String companyIds = companyStreetApplianceService.selectStreetApplianceCompanyId(memberAddress.getStreetId(), memberAddress.getCommunityId());
		if(StringUtils.isBlank(companyIds)){
			memberAddress.setIsDigital("N");
		}else {
			memberAddress.setIsDigital("Y");
		}
		//判断地址是否有公司回收大件
		Integer streetBigCompanyId = companyStreetBigService.selectStreetBigCompanyId(memberAddress.getStreetId());
		if(null != streetBigCompanyId){
			memberAddress.setIsDigThing("Y");
		}else {
			memberAddress.setIsDigThing("N");
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
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	@DS("slave")
    public MemberAddress getMemberAddressById(MemberAddressBean memberAddressBean) {
    	//获取当前登录的会员
    	Member member = MemberUtils.getMember();
    	MemberAddress memberAddress = null;
		if(!StringUtils.isBlank(memberAddressBean.getId())) {
			MemberAddress select = new MemberAddress();
				select.setAliUserId(member.getAliUserId());
				select.setId(Long.parseLong(memberAddressBean.getId()));
			memberAddress = memberAddressService.selectMemberAddressByAliUserIdOne(select);
		}else {
			memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
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
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object updateIsSelectedAddress(MemberAddressBean memberAddressBean) {
		//获取当前登录的会员
		Member member = MemberUtils.getMember();
		return memberAddressService.updateIsSelectedAddress(member.getAliUserId(),memberAddressBean.getId());

    }

	/**
	 * 小程序保存用户的新增地址/修改后保存的地址
	 * @author 王灿
	 * @param
	 * @return
	 */
	@Api(name = "memberAddress.saveMemberAddressd", version = "1.0")
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public String saveMemberAddressd(MemberAddressBean memberAddressBean) {
		//获取当前登录的会员
		Member member = MemberUtils.getMember();
		memberAddressBean.setAliUserId(member.getAliUserId());
		return memberAddressService.saveMemberAddressd(memberAddressBean);
	}

	/**
	 * 根据地图获取的信息保存地址
	 * @author 王灿
	 * @param
	 * @return
	 */
	@Api(name = "memberAddress.saveMemberAddressdByMap", version = "1.0")
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public String saveMemberAddressdByMap(MapAddressBean mapAddressBean) {
		//获取当前登录的会员
		Member member = MemberUtils.getMember();
		return memberAddressService.saveMemberAddressdByMap(mapAddressBean,member.getAliUserId());
	}
    
}
