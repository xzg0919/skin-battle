package com.tzj.collect.api.business;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.core.param.admin.AdminCommunityBean;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.admin.RecyclersBean;
import com.tzj.collect.core.param.business.CompanyAccountBean;
import com.tzj.collect.core.result.business.BusinessRecType;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class BusinessCompanyApi {

	@Autowired
	private RecyclersService recyclersService;
	@Autowired
	private CompanyRecyclerService comRecService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private CommunityService communityService;
	/**
	 * 根据公司id查询回收人员Page（可传<回收人员身份证或姓名>）
	 * 
	 * @param companyAccountBean
	 * @return
	 */
	@Api(name = "business.company.search", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Page<Recyclers> getRecPageByIds(CompanyAccountBean companyAccountBean) {
		String recyclerIds = "";
		List<CompanyRecycler> comRecyclersList = comRecService.selectRecByComId(companyAccountBean.getId());
		if (comRecyclersList.size() > 0) {
			for (CompanyRecycler companyRecycler : comRecyclersList) {
				recyclerIds += companyRecycler.getRecyclerId() + ",";
			}
			recyclerIds = recyclerIds.substring(0, recyclerIds.length() - 1);
		}
		//System.out.println(recyclerIds);
		Page<Recyclers> orderPage = recyclersService.selectRecPageByIds(recyclerIds, companyAccountBean);
		return orderPage;
	}
	/**
	 * 根据公司id得到公司回收类型
	 * @param companyAccountBean
	 * @return
	 */
	@Api(name = "business.company.gettypebycomid", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<BusinessRecType> getTypeByComId(CompanyAccountBean companyAccountBean) {
		List<BusinessRecType> list =  companyService.getTypeByComId(companyAccountBean.getId());
		return list; 
	}
	/**
	 * 根据回收人员id得到个人信息，评价数目
	 * @author sgmark@aliyun.com
	 * @param recyclersBean
	 * @return
	 */
	@Api(name = "business.company.getrecbyid", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public RecyclersBean getRecEvaById(RecyclersBean recyclersBean) {
		RecyclersBean recycler = recyclersService.getRecEvaById(recyclersBean.getRecyclerId().toString());
		return recycler;
	}
	/**
	 * 根据回收人员id得到服务小区名称，所属区域
	 * @author sgmark@aliyun.com
	 * @param recyclersBean
	 * @return
	 */
	@Api(name = "business.company.getrecsercommbyid", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<AdminCommunityBean> getRecSerCommById(RecyclersBean recyclersBean) {
		return recyclersService.getRecSerCommById(recyclersBean.getRecyclerId().toString());
	}
	/**
	 * 根据回收人员id得到服务小区总数
	 * @author sgmark@aliyun.com
	 * @param recyclersBean
	 * @return
	 */
	@Api(name = "business.company.getcommnumbyrecid", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Integer getCommNumByRecId(RecyclersBean recyclersBean) {
		return recyclersService.getCommNumByRecId(recyclersBean.getRecyclerId().toString());
	}
	/**
	 * 获取企业服务范围（例南京市，苏州市等）
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.company.getCompanyRange", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getCompanyRange() {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return comRecService.getCompanyRange(companyAccount.getCompanyId());
	}
	/**
	 * 根据名称获取企业服务范围（例南京市，苏州市等）
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.company.getCompanyRange.code", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getCompanyRangeByCode(com.tzj.collect.core.param.business.CompanyBean companyBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		//要查找的关键字 companyBean.name
		List<Map<String,Object>> mapList = (List<Map<String, Object>>) comRecService.getCompanyRange(companyAccount.getCompanyId());
		return mapList.stream().filter(stringObjectMap -> stringObjectMap.get("area_name").toString().contains(companyBean.getName())).collect(Collectors.toList());
	}

//	@Api(name = "iot.area.iot", version = "1.0")
//	@AuthIgnore
////	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
//	public void  updateArea(){
//		Long current = System.currentTimeMillis();
//		System.out.println(current);
//		List<Area> areaList = areaService.selectList(new EntityWrapper<Area>().eq("del_flag", 0));
//		areaList.parallelStream().forEach(area -> {
//			Area areaOld = area;
//			String pareatIds = area.getParentId().toString() + "_";
//			while (area.getParentId() != 0){
//				area = areaService.selectById(area.getParentId());
//				if (!area.getParentId().toString().equals("0")){
//					pareatIds =  area.getParentId()+"_" + pareatIds;
//				}else {
////					System.out.println("我要更新咯，更新数据是："+pareatIds);
//					areaOld.setParentIds(pareatIds);
//					areaService.updateById(areaOld);
//				}
//			}
//		});
//		System.out.println(System.currentTimeMillis()-current);
//	}


	/**
	 * 获取大件企业服务范围（例南京市，苏州市等）
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.company.getBigCompanyRange", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getBigCompanyRange() {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return comRecService.getBigCompanyRange(companyAccount.getCompanyId());
	}
	/**
	 * 根据市级Id获取市级下属行政区
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.company.getAreasById", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	@DS("slave")
	public Object getAreasById(CompanyBean companyBean) {
		List<Map<String,Object>> resultList = new ArrayList<>();
		List<Area> areaList = areaService.selectList(new EntityWrapper<Area>().eq("parent_id", companyBean.getCityId()));
		return areaList;
	}
	/**
	 * 根据市级Id获取市级下属行政区
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.company.getStreetByAreaId", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	@DS("slave")
	public Object getStreetByAreaId(CompanyBean companyBean) {
		List<Map<String,Object>> resultList = new ArrayList<>();
		List<Area> areaList = areaService.selectList(new EntityWrapper<Area>().eq("parent_id",companyBean.getAreaId()));
		for (Area area: areaList) {
			Map<String,Object> resultMap = new HashMap<>();
			List<Community> communityList = communityService.selectList(new EntityWrapper<Community>().eq("area_id", area.getId()));
			resultMap.put("communityList",communityList);
			resultMap.put("areaName",area.getAreaName());
			resultMap.put("id",area.getId());
			resultList.add(resultMap);
		}
		return resultList;
	}

	/**
	 * 是否打开自定派单
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.company.isOpenOrder", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object isOpenOrder(CompanyBean companyBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return companyService.isOpenOrder(companyBean.getIsOpenOrder(),companyAccount.getCompanyId());
	}

	/**
	 * 公司开通区域信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.company.areaRanges", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object companyAreaRanges(CompanyBean companyBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return companyService.companyAreaRanges(companyBean.getTitle(),companyAccount.getCompanyId().toString());
	}

	/**
	 * 公司开通区域信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.company.getIsOpenOrder", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	@DS("slave")
	public Object getIsOpenOrder() {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		Map<String,Object> resultMap = new HashMap<>();
		Company company = companyService.selectById(companyAccount.getCompanyId());
		if (null != company.getIsOpenOrder()){
			resultMap.put("isOpenOrder",company.getIsOpenOrder());
		}else {
			resultMap.put("isOpenOrder",0);
		}
		return resultMap;
	}
}
