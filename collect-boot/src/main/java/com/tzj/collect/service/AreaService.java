package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.param.RecyclersServiceRangeBean;
import com.tzj.collect.entity.Area;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AreaService  extends IService<Area>{
	/**
	 * @author 王灿
	 * 根据城市id查询所属区域列表
	 * @param level : 区域级别
	 * @param cityId : 城市id
	 * @return
	 */
	@DS("slave")
	List<Area> getByArea(int level,String cityId);
	
	//根据父级取得所有子地区
	@DS("slave")
	List<Area> getChildArea(Long id );
	/**
	 * 根据区县 街道 小区 搜索 都不是必传条件 
	 * @author sgmark@aliyun.com
	 * @return
	 */
	@DS("slave")
	List<Area> selectAreaByCouOrStrOrCom(String county,String street, String village);
	@DS("slave")
	List<Area> selectByNameCityId(String streetName,String cityId);

    String updateAreaAll();

	void inputAreaCode(List<Map<String, String>> mapList) throws ApiException;

	String updateAreaParent();
	@DS("slave")
	Object adminGetCityList(String name);

	@DS("slave")
	Object adminGetAreaRange(Integer companyId,Integer cityId,String title);

	@DS("slave")
	Object adminGetStreetRange(Integer companyId,Integer areaId,String title);

	Object updateOrSaveCompanyRange( RecyclersServiceRangeBean recyclersServiceRangeBean);
	@DS("slave")
	Object getHouseRangeList(Integer companyId, PageBean pageBean);

	Object saveOrUpdateCommunity(Integer companyId,String location) throws Exception;

	Object deleteCommunityByIds(List<String>  communityIds);
}
