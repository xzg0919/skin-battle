package com.tzj.collect.mapper;

import java.util.List;
import java.util.Map;

import com.tzj.collect.controller.admin.param.StreetNameBean;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.Area;

public interface AreaMapper extends BaseMapper<Area> {
	List<Area> selectAreaByCouOrStrOrCom(@Param("countyId") String countyId, @Param("streetId") String streetId,
			@Param("communityId") String communityId);
	
	Map<String,Object> getAred(String id);

	List<Area> selectByNameCityId(@Param("streetName") String streetName,@Param("cityId") String cityId);

	List<Map<String, String>> allAreaList(@Param("parentId")String parentId);

	List<Map<String, Object>> selectAreaParent();

	List<Map<String, Object>> adminGetCityList(@Param("name")String name);

	List<Map<String, Object>> adminGetApplianceAreaRange(@Param("companyId") Integer companyId,@Param("cityId")Integer cityId);

	List<Map<String, Object>> adminGetHouseAreaRange(@Param("companyId") Integer companyId,@Param("cityId")Integer cityId);

	List<Map<String, Object>> adminGetBigAreaRange(@Param("companyId") Integer companyId,@Param("cityId")Integer cityId);

	List<Map<String, Object>> adminGetApplianceStreetRange(@Param("companyId") Integer companyId,@Param("areaId")Integer areaId);

	List<Map<String, Object>> adminGetHouseStreetRange(@Param("companyId") Integer companyId,@Param("areaId")Integer areaId);

	List<Map<String, Object>> adminGetBigStreetRange(@Param("companyId") Integer companyId,@Param("areaId")Integer areaId);

	List<Map<String, Object>> getHouseRangeList(@Param("companyId") Integer companyId,@Param("pageStart") Integer pageStart,@Param("pageSize")Integer pageSize);

	Integer getHouseRangeCount(@Param("companyId") Integer companyId);

	List<StreetNameBean> selectStreetList();

	List<StreetNameBean> selectStreetListByName(@Param("name")String name,@Param("code")String code);

	Integer updateStreet(@Param("id") String id,@Param("name")String name,@Param("code")String code);
}
