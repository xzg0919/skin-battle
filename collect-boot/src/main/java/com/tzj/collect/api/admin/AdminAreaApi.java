package com.tzj.collect.api.admin;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.admin.AdminCommunityBean;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.business.RecyclersServiceRangeBean;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.CommunityService;
import com.tzj.collect.core.service.CompanyService;
import com.tzj.collect.core.service.RecyclerCommunityService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.Recyclers;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * 管理员小区管理
 * 
 * @author sgmark@aliyun.com
 *
 */
@ApiService
public class AdminAreaApi {

	@Autowired
	private RecyclerCommunityService recComService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private CommunityService commService;


	/**
	 * 联动下拉选择框地址数据
	 * @author: sgmark@aliyun.com
	 * @Date: 2019/12/23 0023
	 * @Param: 
	 * @return: 
	 */
	@Api(name = "admin.all.area.info", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Map<String, Object> allAreaStreetIdNameInfo() {
		return areaService.allAreaStreetIdNameInfo();
	}
	// 根据区县 街道 小区 搜索 都不是必传条件 分别特
	@Api(name = "admin.area.getarealist", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<Area> getAreaList(AdminCommunityBean adminCommunityBean) {
		return areaService.selectAreaByCouOrStrOrCom(adminCommunityBean.getCountyId(),
				adminCommunityBean.getStreetId(), adminCommunityBean.getCommunityId());
	}

	/**
	 * 根据小区id得到回收人员信息
	 * @author sgmark@aliyun.com
	 * @return
	 */
	@Api(name = "admin.area.getrecbycommid", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<Recyclers> getRecByCommId(AdminCommunityBean adminCommunityBean) {
		List<Recyclers> recycler = commService.getRecByCommId(adminCommunityBean.getCommunityId());
		return recycler;
	}
	
	// 根据小区id 取得回收员人数
	@Api(name = "admin.area.getrecnum", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Integer getAreaRecyclerNum(AdminCommunityBean adminCommunityBean) {
		return recComService.selectRecCountByCom(adminCommunityBean.getCommunityId());
	}

	// 根据小区id 取得回收企业列表
	@Api(name = "admin.area.getcomlist", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<Company> getAreaCompanyList(AdminCommunityBean adminCommunityBean) {
		return companyService.selectCompanyListByComm(adminCommunityBean.getCommunityId());
	}
	// 根据小区id 取得回收企业数
	@Api(name = "admin.area.getcomnum", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Integer getAreaCompanyNum(AdminCommunityBean adminCommunityBean) {
		return companyService.selectCompanyCountByCom(adminCommunityBean.getCommunityId());
	}

	// 根据小区id 取得回收企业数 然后统计总的回收类型 去除重复后统计
	@Api(name = "admin.area.getcatenum", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Integer getCategoryNum(AdminCommunityBean adminCommunityBean) {
		return companyService.selectCompanyCountByCom(adminCommunityBean.getCommunityId());
	}

	// 根据所有的城市列表
	@Api(name = "admin.company.adminGetCityList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object adminGetCityList(CompanyBean companyBean) {
		return areaService.adminGetCityList(companyBean.getName());
	}

	// 根据服务商Id和城市Id查询相关的行政区域信息
	@Api(name = "admin.company.adminGetAreaRange", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object adminGetAreaRange(CompanyBean companyBean) {
		return areaService.adminGetAreaRange(companyBean.getId().intValue(),companyBean.getCityId(),companyBean.getTitle());
	}
	// 根据服务商Id和行政区Id查询相关的街道信息
	@Api(name = "admin.company.adminGetStreetRange", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object adminGetStreetRange(CompanyBean companyBean) {
		return areaService.adminGetStreetRange(companyBean.getId().intValue(),companyBean.getAreaId(),companyBean.getTitle());
	}

	// 根据服务商Id和行政区Id查询相关的街道信息
	@Api(name = "admin.company.updateOrSaveCompanyRange", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object updateOrSaveCompanyRange(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		return areaService.updateOrSaveCompanyRange(recyclersServiceRangeBean);
	}

	// 根据服务商Id获取生活垃圾小区的列表
	@Api(name = "admin.company.getHouseRangeList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getHouseRangeList(CompanyBean companyBean) {
		return areaService.getHouseRangeList(companyBean.getId().intValue(),companyBean.getPageBean());
	}

	// 根据经纬度保存公司关联地址
	@Api(name = "admin.company.saveOrUpdateCommunity", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object saveOrUpdateCommunity(CompanyBean companyBean)throws Exception {
		return areaService.saveOrUpdateCommunity(companyBean.getId().intValue(),companyBean.getLocation());
	}

	// 根据删除小区信息
	@Api(name = "admin.company.deleteCommunityByIds", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object deleteCommunityByIds(CompanyBean companyBean)throws Exception {
		return areaService.deleteCommunityByIds(companyBean.getCommunityIds());
	}

	@Api(name = "admin.company.isOpenCompanyByCategory", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object isOpenCompanyByCategory(CompanyBean companyBean)throws Exception {
		return areaService.isOpenCompanyByCategory(companyBean.getId().toString(),companyBean.getIsOpen(),companyBean.getTitle());
	}
	/*
	 *根据公司Id和类型查询所属街道列表
	 */
	@Api(name = "admin.company.getAreaStreetList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getAreaStreetList(CompanyBean companyBean)throws Exception {
		return areaService.getAreaStreetList(companyBean);
	}
	/**
	 * 获取所有城市列表
	 * @param
	 * @return
	 */
	@Api(name = "admin.getCityAllList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getCityAllList(){
		List<Area> cityList = areaService.getCityListByLj();
		return cityList;
	}
	/**
	 * 根据城市Id获取相关行政区
	 * @param
	 * @return
	 */
	@Api(name = "admin.getAreaListBycityId", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getAreaListBycityId(AreaBean areaBean){
		List<Area> areaList = null;
		if(StringUtils.isNotBlank(areaBean.getCityId())){
			areaList = areaService.selectList(new EntityWrapper<Area>().eq("parent_id", areaBean.getCityId()));
		}
		return areaList;
	}

	/**
	 * 获取服务商回收区域列表
	 * @param
	 * @return
	 */
	@Api(name = "admin.getCompanyServiceList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getCompanyServiceList(AreaBean areaBean){
		return areaService.getCompanyServiceList(areaBean);
	}
	/**
	 * 根据公司Id和行政区Id查询相关街道
	 * @param
	 * @return
	 */
	@Api(name = "admin.getCompanyStreetAllList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getCompanyStreetAllList(AreaBean areaBean){
		return areaService.getCompanyStreetAllList(areaBean);
	}

	/**
	 * 更新公司的服务范围
	 * @param
	 * @return
	 */
	@Api(name = "admin.updateCompanyServiceByStreetId", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object updateCompanyServiceByStreetId(AreaBean areaBean){
		return areaService.updateCompanyServiceByStreetId(areaBean);
	}

	/**
	 * 获取省级列表
	 * @param
	 * @return
	 */
	@Api(name = "admin.provinceList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object provinceList(AreaBean areaBean){
		return areaService.provinceList(areaBean);
	}
	/**
	 * 根据省级Id获取市级列表
	 * @param
	 * @return
	 */
	@Api(name = "admin.cityListByProvinceId", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object cityListByProvinceId(AreaBean areaBean){
		return areaService.cityListByProvinceId(areaBean);
	}

	/**
	 * 根据城市Id更改城市系数
	 * @param
	 * @return
	 */
	@Api(name = "admin.updateCityRatio", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object updateCityRatio(AreaBean areaBean) throws Exception{
		return areaService.updateCityRatio(areaBean);
	}

	/**
	 * 根据服务商Id获取服务商服务的所有城市
	 * @param
	 * @return
	 */
	@Api(name = "admin.getAllCityListByCompanyId", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getAllCityListByCompanyId(AreaBean areaBean) throws Exception{
		return areaService.getAllCityListByCompanyId(areaBean);
	}
	/**
	 * 根据服务商ID和城市Id获取服务商回收的类型
	 * @param
	 * @return
	 */
	@Api(name = "admin.getTitleByCompanyId", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getTitleByCompanyId(AreaBean areaBean) throws Exception{
		return areaService.getTitleByCompanyId(areaBean);
	}
}
