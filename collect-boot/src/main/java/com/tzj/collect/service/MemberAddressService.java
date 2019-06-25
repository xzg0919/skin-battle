package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.param.MapAddressBean;
import com.tzj.collect.api.ali.param.MemberAddressBean;
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
	public List<MemberAddress> memberAddressList(long memberId,String cityId);
	/**
	 * 用户删除地址时，如果是删除默认地址，自动将余下任意一条地址设置为默认地址
	 * @author wangcan
	 * @param memberAddressId : 地址id
	 * @return
	 */
	public String deleteByMemberId(String memberAddressId,long memberId);

	/**
	 * 小程序保存用户的新增地址
	 * @author 王灿
	 * @param
	 * @return
	 */
	public String saveMemberAddressd(MemberAddressBean memberAddressBean);

	String saveMemberAddressdByMap(MapAddressBean mapAddressBean,long memberId);
}
