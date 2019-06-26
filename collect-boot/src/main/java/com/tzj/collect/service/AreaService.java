package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Area;

import java.util.List;

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
}
