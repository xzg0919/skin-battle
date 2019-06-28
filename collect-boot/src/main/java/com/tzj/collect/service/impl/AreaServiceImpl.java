package com.tzj.collect.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.api.ali.AmapApi;
import com.tzj.collect.api.ali.param.AreaBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.ali.result.AmapResult;
import com.tzj.collect.api.business.param.CommunityBean;
import com.tzj.collect.api.business.param.RecyclersServiceRangeBean;
import com.tzj.collect.entity.*;
import com.tzj.collect.mapper.AreaMapper;
import com.tzj.collect.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

@Service
@Transactional(readOnly=true)
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService{

	@Resource
	private AreaMapper mapper;
	@Autowired
	private CompanyStreetApplianceService companyStreetApplianceService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private CompanyServiceService companyServiceService;
	
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

	@Override
	@Transactional(readOnly = false)
	public void inputAreaCode(List<Map<String, String>> mapList) throws ApiException {
		AtomicReference<Boolean> flag = new AtomicReference<>(true);
		mapList.stream().forEach(mapLists->{
			//id存在时，更新code
			Area areaParent = mapper.selectList(new EntityWrapper<Area>().eq("code_", mapLists.get("parentCode"))).get(0);
			if (null != mapLists.get("id")){
				Area area = mapper.selectById(mapLists.get("id"));
				area.setParentId(areaParent.getId().intValue());
				area.setParentIds(areaParent.getParentIds()+areaParent.getId()+"_");
				area.setCode(mapLists.get("code"));
				mapper.updateById(area);
			}else {
				//父级
				if (null == areaParent){
					flag.set(false);
					System.out.println(mapLists.get("parentCode"));
					return;
				}else {
					//新增区
					Area area1 = new Area();
					area1.setAreaName(mapLists.get("parent"));
					area1.setParentId(areaParent.getId().intValue());
					area1.setParentIds(areaParent.getParentIds()+areaParent.getId()+"_");
					area1.setCode(mapLists.get("code"));
					area1.setSort_(1);
					area1.setType("2");
					mapper.insert(area1);
				}
			}
		});
		if (!flag.get()){
			throw new ApiException("市级没找到");
		}
	}

	/**	更新parentIds
	  * @author sgmark@aliyun.com
	  * @date 2019/6/27 0027
	  * @param
	  * @return
	  */
	@Override
	@Transactional(readOnly = false)
	public String updateAreaParent() {
		List<Map<String, Object>> mapList = mapper.selectAreaParent();
		mapList.stream().forEach(mapLists->{
			Area updateArea = mapper.selectById(mapLists.get("id").hashCode());
			updateArea.setParentIds(mapLists.get("alPid")+"_"+mapLists.get("pId")+"_"+mapLists.get("parent_id")+"_");
			updateArea.setUpdateDate(new Date());
			mapper.updateById(updateArea);
		});
		return null;
	}

	@Override
	public Object adminGetCityList(String name){

		return mapper.adminGetCityList(name);

	}

	@Override
	public Object adminGetAreaRange(Integer companyId,Integer cityId,String title){
		List<Map<String, Object>> resultList = null;
		List<Map<String, Object>> streetList = null;
		if ("1".equals(title)) {
			resultList = mapper.adminGetApplianceAreaRange(companyId,cityId);
		}else if("4".equals(title)){
			resultList = mapper.adminGetBigAreaRange(companyId,cityId);
		}
			return resultList;

	}
	@Override
	public Object adminGetStreetRange(Integer companyId,Integer areaId,String title){
		List<Map<String, Object>> streetList = null;
		if ("1".equals(title)) {
			streetList = mapper.adminGetApplianceStreetRange(companyId,areaId);
		}else if ("4".equals(title)){
			streetList = mapper.adminGetBigStreetRange(companyId,areaId);
		}
		return streetList;
	}

	@Transactional
	@Override
	public Object updateOrSaveCompanyRange( RecyclersServiceRangeBean recyclersServiceRangeBean){
		Integer companyId = recyclersServiceRangeBean.getCompanyId();

		//获取所有的区域Id
		List<AreaBean> areaList = recyclersServiceRangeBean.getAreaList();
		for (AreaBean areaBean: areaList) {
			if(null != areaBean){
				if("1".equals(recyclersServiceRangeBean.getTitle())){
					if("0".equals(areaBean.getSaveOrDelete())){
						CompanyStreetAppliance companyStreetAppliance = companyStreetApplianceService.selectOne(new EntityWrapper<CompanyStreetAppliance>().eq("street_id", areaBean.getStreeId()));
						if(null == companyStreetAppliance){
							companyStreetAppliance = new CompanyStreetAppliance();
							companyStreetAppliance.setCompanyId(companyId);
							companyStreetAppliance.setStreetId(Integer.parseInt(areaBean.getStreeId()));
							companyStreetAppliance.setAreaId(Integer.parseInt(areaBean.getAreaId()));
							companyStreetApplianceService.insert(companyStreetAppliance);
						}
					}else {
						companyStreetApplianceService.delete(new EntityWrapper<CompanyStreetAppliance>().eq("company_id",companyId).eq("street_id",areaBean.getStreeId()));
					}
				}else if("4".equals(recyclersServiceRangeBean.getTitle())){
					if("0".equals(areaBean.getSaveOrDelete())){
						CompanyStreetBig companyStreetBig = companyStreetBigService.selectOne(new EntityWrapper<CompanyStreetBig>().eq("street_id", areaBean.getStreeId()));
						if(null == companyStreetBig){
							companyStreetBig = new CompanyStreetBig();
							companyStreetBig.setCompanyId(companyId);
							companyStreetBig.setStreetId(Integer.parseInt(areaBean.getStreeId()));
							companyStreetBig.setAreaId(Integer.parseInt(areaBean.getAreaId()));
							companyStreetBigService.insert(companyStreetBig);
						}
					}else {
						companyStreetBigService.delete(new EntityWrapper<CompanyStreetBig>().eq("company_id",companyId).eq("street_id",areaBean.getStreeId()));
					}
				}
			}
		}
		return  "操作成功";
	}

	@Override
	public Object getHouseRangeList(Integer companyId, PageBean pageBean){
		int pageStart = (pageBean.getPageNumber() - 1) * pageBean.getPageSize();
		Map<String,Object> resultMap = new HashMap<>();
		List<Map<String,Object>> houseList = new ArrayList<>();
		houseList = mapper.getHouseRangeList(companyId,pageStart,pageBean.getPageSize());
		Integer count = mapper.getHouseRangeCount(companyId);
		resultMap.put("pageNum",pageBean.getPageNumber());
		resultMap.put("count",count);
		resultMap.put("houseList",houseList);
		return resultMap;
	}

	@Override
	@Transactional
	public Object saveOrUpdateCommunity(Integer companyId,String location)throws Exception{

		AmapResult amap = AmapApi.getAmap(location);
		CompanyServiceRange companyServiceRange = null;
		if(null!= amap){
			Area area = this.selectOne(new EntityWrapper<Area>().eq("code_", amap.getTowncode()));
			if(null==area){
				throw new Exception("该小区找不到对应的街道");
			}else {
				Community community = communityService.selectOne(new EntityWrapper<Community>().eq("area_id", area.getId()).eq("name_", amap.getName()));
				if(null != community){
					companyServiceRange = companyServiceService.selectOne(new EntityWrapper<CompanyServiceRange>().eq("company_id", companyId).eq("community_id", community.getId()));
					if(companyServiceRange != null){
						throw new Exception("该小区已经有相关企业关联");
					}else {
						companyServiceRange = new CompanyServiceRange();
						companyServiceRange.setCompanyId(companyId.toString());
						companyServiceRange.setCommunityId(community.getId().intValue());
						companyServiceRange.setAreaId(community.getAreaId());
						companyServiceRange.setParentIds(community.getParentIds());
						companyServiceService.insert(companyServiceRange);
					}
				}else {
					community = new Community();
					community.setAreaId(area.getId().intValue());
					community.setParentIds(area.getParentIds());
					community.setName(amap.getName());
					community.setAddress(amap.getAddress());
					community.setLongitude(Double.parseDouble(location.split(",")[0]));
					community.setLatitude(Double.parseDouble(location.split(",")[1]));
					communityService.insert(community);
					companyServiceRange = new CompanyServiceRange();
					companyServiceRange.setCompanyId(companyId.toString());
					companyServiceRange.setCommunityId(community.getId().intValue());
					companyServiceRange.setAreaId(community.getAreaId());
					companyServiceRange.setParentIds(community.getParentIds());
					companyServiceService.insert(companyServiceRange);
				}
			}
			amap.getTowncode();

		}
		return "操作成功";
	}

	@Override
	@Transactional
	public  Object deleteCommunityByIds(List<String>  communityIds){
		if(!communityIds.isEmpty()){
			for (String communityId:communityIds){
				if(StringUtils.isNotBlank(communityId)){
					communityService.deleteById(Long.parseLong(communityId));
					companyServiceService.delete(new EntityWrapper<CompanyServiceRange>().eq("community_id",communityId));
				}
			}
		}
		return "操作成功";
	}

}
