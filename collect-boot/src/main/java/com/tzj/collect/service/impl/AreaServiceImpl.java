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
import java.util.Map;

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

	@Override
	@Transactional(readOnly = false)
	public String updateAreaAll() {
		//查询省
		List<Area> areaList = mapper.selectList(new EntityWrapper<Area>().eq("del_flag", 0).eq("parent_id", 0));
		areaList.stream().forEach(parentArea -> {
			List<Map<String, String>> allAreaList =  mapper.allAreaList(parentArea.getId()+"_%");
			allAreaList.stream().forEach(areaLists ->{
				if (!parentArea.getCode().equals(areaLists.get("pri_code"))){
					return;
				}
				Area parentAreaId = mapper.selectList(new EntityWrapper<Area>().setSqlSelect("*").eq("code_", areaLists.get("zone_code"))).get(0);
				if (null != areaLists.get("id") && null != areaLists.get("area_name")){
					Area area = mapper.selectById(areaLists.get("id"));
					if (!parentAreaId.getParentIds().equals(parentAreaId.getParentIds() + parentAreaId.getId().toString()+"_")){
						area.setParentId(parentAreaId.getId().intValue());
						area.setParentIds(parentAreaId.getParentIds() + parentAreaId.getId().toString()+"_");
						area.setCode(areaLists.get("code"));
					}
					//如果能找到直接更新
					area.setCode(areaLists.get("code"));
					mapper.updateById(area);
				}else {
					//根据区code查询当前区，当前区parent_ids+'_'+id 新增至数据库中
					Area area = new Area();
					area.setAreaName(areaLists.get("name_"));
					area.setParentId(parentAreaId.getId().intValue());
					area.setParentIds(parentAreaId.getParentIds() + parentAreaId.getId().toString()+"_");
					area.setCode(areaLists.get("code"));
					area.setType("3");
					area.setSort_(1);
					mapper.insert(area);
				}
			});
		});
		return null;
	}

}
