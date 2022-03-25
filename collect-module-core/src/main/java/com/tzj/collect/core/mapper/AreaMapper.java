package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.business.StreetNameBean;
import com.tzj.collect.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AreaMapper extends BaseMapper<Area> {
	List<Area> selectAreaByCouOrStrOrCom(@Param("countyId") String countyId, @Param("streetId") String streetId,
                                         @Param("communityId") String communityId);

	Map<String,Object> getAred(String id);

	List<Area> selectByNameCityId(@Param("streetName") String streetName, @Param("cityId") String cityId);

	List<Map<String, String>> allAreaList(@Param("parentId") String parentId);

	List<Map<String, Object>> selectAreaParent();

	List<Map<String, Object>> adminGetCityList(@Param("name") String name);

	List<Map<String, Object>> adminGetApplianceAreaRange(@Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

	List<Map<String, Object>> adminGetElectroAreaRange(@Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

	List<Map<String, Object>> adminGetHouseAreaRange(@Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

	List<Map<String, Object>> adminGetBigAreaRange(@Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

	List<Map<String, Object>> adminGetApplianceStreetRange(@Param("companyId") Integer companyId, @Param("areaId") Integer areaId);

	List<Map<String, Object>> adminGetElectroStreetRange(@Param("companyId") Integer companyId, @Param("areaId") Integer areaId);

	List<Map<String, Object>> adminGetHouseStreetRange(@Param("companyId") Integer companyId, @Param("areaId") Integer areaId);

	List<Map<String, Object>> adminGetBigStreetRange(@Param("companyId") Integer companyId, @Param("areaId") Integer areaId);

	List<Map<String, Object>> getHouseRangeList(@Param("companyId") Integer companyId, @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);

	Integer getHouseRangeCount(@Param("companyId") Integer companyId);

	List<StreetNameBean> selectStreetList();

	List<StreetNameBean> selectStreetListByName(@Param("name") String name, @Param("code") String code);

	Integer updateStreet(@Param("id") String id, @Param("name") String name, @Param("code") String code);

	List<Area> getCityListByLj();

	List<Map<String, Object>> getCompanyServiceList(@Param("companyId")String companyId,@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("pageStart") Integer pageStart,@Param("pageSize")Integer pageSize);

	Integer getCompanyServiceCount (@Param("companyId")String companyId,@Param("cityId")String cityId,@Param("areaId")String areaId);

	List<Map<String, Object>> getCompanyStreetAllList(@Param("companyId")String companyId,@Param("areaId")String areaId);

	List<Map<String, Object>> getCompanyServiceOutList(@Param("companyId")String companyId,@Param("cityId")String cityId,@Param("areaId")String areaId);

	List<Map<String, Object>> getCityListByGary();

	List<Map<String, Object>> getAreaCityRatioLists(@Param("provinceId")String provinceId,@Param("cityId")String cityId,@Param("pageStart")Integer pageStart,@Param("pageSize")Integer  pageSize);

	Integer getAreaCityRatioCount(@Param("provinceId")String provinceId,@Param("cityId")String cityId);

	List<Map<String, Object>> getAllCityListByCompanyId(@Param("companyId") String companyId);

	Integer getTitleByCompanyId(@Param("tableName")String tableName,@Param("companyId")String companyId,@Param("cityId")String cityId);

	Integer getCategoryTitleByCompanyId(@Param("companyId")String companyId,@Param("title")String title);


	List<String> getAreaRange();

	List<Area> getProvinceRange();

	List<Area> getCityRange();

	List<String> selectByParentId(@Param("parentId") Long parentId);

}
