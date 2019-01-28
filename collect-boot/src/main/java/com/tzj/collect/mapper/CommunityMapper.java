package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.Recyclers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommunityMapper extends BaseMapper<Community> {
	
	
	/**
	 * 根据街道id获取小区列表并根据用户的经纬度径行由近到远的排序
	 * @author wangcan
	 * @param areaId : 街道Id
	 * @param longitude : 经度
	 * @param latitude : 纬度
	 */
	List<Community> areaCommunityList(@Param("areaId")Integer areaId,@Param("longitude")double longitude,@Param("latitude")double latitude);
	
	 List<Community> listareaByCategory(@Param("categoryId") Integer categoryId,@Param("areaId") Integer areaId); 
	 
	 Community defaultAddress(@Param("communityId") Integer communityId,@Param("categoryId") Integer categoryId);
	 /**
     * 根据小区id得到回收人员信息
     * @param commId
     * @return
     */
	 List<Recyclers> getRecByCommId(@Param("commId") String commId);
/**
 * 新增公司时根据回收类型和服务范围过滤已被添加的小区
* @Title: selectCommunityByConditions
* @date 2018年3月30日 上午11:12:21
* @author:[王池]
* @param @param companybean
* @param @return    参数
* @return List<AdminCommunityBean>    返回类型
 */
	List<AdminCommunityBean> selectCommunityByConditions(@Param("companybean") CompanyBean companybean);

	List<AdminCommunityBean> getSelectedCommunityListByCompanyId(@Param("companyId") long id);

	List<AdminCommunityBean> getEditorCommunity(@Param("companybeanEditor") CompanyBean companybean);
	/**
     * 根据企业id得到服务范围
     * @param companyId : 企业id
     * @return
     */
    List<Area> getCommunityData(String companyId);
}
