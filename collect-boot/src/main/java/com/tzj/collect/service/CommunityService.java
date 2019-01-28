package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.Recyclers;

import java.util.List;

public interface CommunityService extends IService<Community> {

	List<Community> areaCommunity(Integer areaId);
	
	/**
	 * 根据街道id获取小区列表并根据用户的经纬度径行由近到远的排序
	 * @author wangcan
	 * @param areaId : 街道Id
	 * @param longitude : 经度
	 * @param latitude : 纬度
	 */
	List<Community> areaCommunityList(Integer areaId,double longitude,double latitude);
	
	List<Community> listareaByCategory(Integer categoryId,Integer areaId);
	
    Community defaultAddress(Integer communityId,Integer categoryId);

	
	/**根据回收类型和服务范围过滤已被添加的小区
	* @Title: selectCommunityByConditions
	* @Description: 【】
	* @date 2018年3月21日 上午11:28:51
	* @author:[王池][wjc2013481273@163.com]
	* @param @param companybean 
	* @param @return 小区集合
	* @return List<Community>    返回类型
	* @throws
	*/
	
	List<AdminCommunityBean> selectCommunityByConditions(CompanyBean companybean);

	/**
	 *  根据公司id获取服务范围的小区列表
	* @Title: getSelectedCommunityListByCompanyId
	* @Description: 【】
	* @date 2018年3月22日 上午9:18:20
	* @author:[王池]
	* @param @param id
	* @param @return    参数
	* @return List<Community>    返回类型
	* @throws
	 */
	List<AdminCommunityBean> getSelectedCommunityListByCompanyId(long id);

	/**
	 * 获取编辑页面的小区列表
	* @Title: getEditorCommunity
	* @Description: 【】
	* @date 2018年3月23日 上午9:47:16
	* @author:[王池]
	* @param @param companybean
	* @param @return    参数
	* @return List<AdminCommunityBean>    返回类型
	* @throws
	 */
	List<AdminCommunityBean> getEditorCommunity(CompanyBean companybean);	
    
    /**
     * 根据小区id得到回收人员信息
     * @param commId
     * @return
     */
    List<Recyclers> getRecByCommId(String commId);
    /**
     * 根据企业id得到服务范围
     * @param companyId : 企业id
     * @return
     */
    List<Area> getCommunityData(String companyId);
}
