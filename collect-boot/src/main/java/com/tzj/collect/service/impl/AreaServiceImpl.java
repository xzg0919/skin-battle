package com.tzj.collect.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.entity.Area;
import com.tzj.collect.mapper.AreaMapper;
import com.tzj.collect.service.AreaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional(readOnly=true)
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService{

	@Resource
	private AreaMapper mapper;
	
	@Override
	public List<Area> getByArea(int level,String cityId) {
		return selectList(new EntityWrapper<Area>().eq("type", level).eq("del_flag", "0").eq("parent_id", cityId));
	}

	@Override
	public List<Area> getChildArea(Long id) {
		return selectList(new EntityWrapper<Area>().eq("parent_id", id).eq("del_flag", "0"));
	}

	@Override
	public List<Area> selectAreaByCouOrStrOrCom(String countyId, String streetId, String communityId) {
		return mapper.selectAreaByCouOrStrOrCom(countyId, streetId, communityId);
	}

	@Override
	public List<Area> selectByNameCityId(String streetName, String cityId) {
		return mapper.selectByNameCityId(streetName,cityId);
	}

}
