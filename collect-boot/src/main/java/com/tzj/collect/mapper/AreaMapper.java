package com.tzj.collect.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.Area;

public interface AreaMapper extends BaseMapper<Area> {
	List<Area> selectAreaByCouOrStrOrCom(@Param("countyId") String countyId, @Param("streetId") String streetId,
			@Param("communityId") String communityId);
	
	Map<String,Object> getAred(String id);
}
