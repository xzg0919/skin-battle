package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.business.RecyclersServiceRangeBean;
import com.tzj.collect.core.param.business.StreetNameBean;
import com.tzj.collect.entity.Area;

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
	List<Area> getByArea(int level, String cityId);

	//根据父级取得所有子地区
	@DS("slave")
	List<Area> getChildArea(Long id);
	/**
	 * 根据区县 街道 小区 搜索 都不是必传条件
	 * @author sgmark@aliyun.com
	 * @return
	 */
	@DS("slave")
	List<Area> selectAreaByCouOrStrOrCom(String county, String street, String village);
	@DS("slave")
	List<Area> selectByNameCityId(String streetName, String cityId);

    String updateAreaAll();

	void inputAreaCode(List<Map<String, String>> mapList) throws Exception;

	String updateAreaParent();
	@DS("slave")
	Object adminGetCityList(String name);

	@DS("slave")
	Object adminGetAreaRange(Integer companyId, Integer cityId, String title);

	@DS("slave")
	Object adminGetStreetRange(Integer companyId, Integer areaId, String title);

	Object updateOrSaveCompanyRange(RecyclersServiceRangeBean recyclersServiceRangeBean);
	@DS("slave")
	Object getHouseRangeList(Integer companyId, PageBean pageBean);

	Object saveOrUpdateCommunity(Integer companyId, String location) throws Exception;

	Object deleteCommunityByIds(List<String> communityIds);

	List<StreetNameBean> selectStreetList();

	List<StreetNameBean> selectStreetListByName(String name, String code);

	Integer updateStreet(String id, String name, String code);

	Object isOpenCompanyByCategory(String companyId, String isOpen, String title);

    void addInputAreaCode(List<Map<String, String>> mapList);
	
	Object getAreaStreetList(CompanyBean companyBean);
	@DS("slave")
	List<Area> getCityListByLj();
	@DS("slave")
	Map<String,Object> getCompanyServiceList(AreaBean areaBean);
	@DS("slave")
	List<Map<String, Object>> getCompanyStreetAllList(AreaBean areaBean);

	Object updateCompanyServiceByStreetId(AreaBean areaBean);
	@DS("slave")
	List<Map<String, Object>> getCompanyServiceOutList(AreaBean areaBean);
	@DS("slave")
	List<Map<String, Object>> getCityListByGary();
	@DS("slave")
	List<Area> provinceList(AreaBean areaBean);
	@DS("slave")
	List<Area> cityListByProvinceId(AreaBean areaBean);
	@DS("slave")
	Map<String,Object> getAreaCityRatioLists(AreaBean areaBean);

	String updateCityRatio(AreaBean areaBean)throws Exception;
	@DS("slave")
	List<Map<String,Object>> getAllCityListByCompanyId(AreaBean areaBean);
	@DS("slave")
	Map<String,Object> getTitleByCompanyId(AreaBean areaBean);
}
