package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.MapAddressBean;
import com.tzj.collect.core.param.ali.MemberAddressBean;
import com.tzj.collect.entity.MemberAddress;

import java.util.List;

/**
 * @Author 王灿
 **/
public interface MemberAddressService extends IService<MemberAddress>{
	
	/**
     * 保存用户的新增地址
     * @author 王灿
     * @param 
     * @return 
     */
	public String saveMemberAddress(MemberAddressBean memberAddressBean);
	 /**
     * 用户地址的列表
     * @author 王灿
     * @param 
     * @return 
     */
	@DS("slave")
	public List<MemberAddress> memberAddressList(long memberId);
	/**
	 * 用户删除地址时，如果是删除默认地址，自动将余下任意一条地址设置为默认地址
	 * @author wangcan
	 * @param memberAddressId : 地址id
	 * @return
	 */
	public String deleteByMemberId(String memberAddressId, long memberId);

	/**
	 * 小程序保存用户的新增地址
	 * @author 王灿
	 * @param
	 * @return
	 */
	public String saveMemberAddressd(MemberAddressBean memberAddressBean);

	String saveMemberAddressdByMap(MapAddressBean mapAddressBean, long memberId);

	/**
	 * 小获取用户的默认地址
	 * @author 王灿
	 * @param
	 * @return
	 */
	@DS("slave")
	public MemberAddress getMemberAdderssByMemberId(String memberId);

	Object updateIsSelectedAddress(String memberId, String id);
	@DS("slave")
	String getMemberAddressById(String id);

	@DS("slave")
	List<MemberAddressBean> selectMemberAddressByCommunityId();

	Object updateMemberAddress(String id, String communityId);

	Object MemberAddressUpdateStreetId();
}
