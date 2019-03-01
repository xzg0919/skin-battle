package com.tzj.collect.api.admin;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.service.AreaService;
import com.tzj.collect.service.CommunityService;
import com.tzj.collect.service.CompanyService;
import com.tzj.collect.service.RecyclerCommunityService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

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
	
	// 根据区县 街道 小区 搜索 都不是必传条件 分别特
	@Api(name = "admin.area.getarealist", version = "1.0")
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<Area> getAreaList(AdminCommunityBean adminCommunityBean) {
		return areaService.selectAreaByCouOrStrOrCom(adminCommunityBean.getCountyId(),
				adminCommunityBean.getStreetId(), adminCommunityBean.getCommunityId());
	}

	/**
	 * 根据小区id得到回收人员信息
	 * @author sgmark@aliyun.com
	 * @param recyclersBean
	 * @return
	 */
	@Api(name = "admin.area.getrecbycommid", version = "1.0")
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<Recyclers> getRecByCommId(AdminCommunityBean adminCommunityBean) {
		List<Recyclers> recycler = commService.getRecByCommId(adminCommunityBean.getCommunityId());
		return recycler;
	}
	
	// 根据小区id 取得回收员人数
	@Api(name = "admin.area.getrecnum", version = "1.0")
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Integer getAreaRecyclerNum(AdminCommunityBean adminCommunityBean) {
		return recComService.selectRecCountByCom(adminCommunityBean.getCommunityId());
	}

	// 根据小区id 取得回收企业列表
	@Api(name = "admin.area.getcomlist", version = "1.0")
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<Company> getAreaCompanyList(AdminCommunityBean adminCommunityBean) {
		return companyService.selectCompanyListByComm(adminCommunityBean.getCommunityId());
	}
	// 根据小区id 取得回收企业数
	@Api(name = "admin.area.getcomnum", version = "1.0")
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Integer getAreaCompanyNum(AdminCommunityBean adminCommunityBean) {
		return companyService.selectCompanyCountByCom(adminCommunityBean.getCommunityId());
	}

	// 根据小区id 取得回收企业数 然后统计总的回收类型 去除重复后统计
	@Api(name = "admin.area.getcatenum", version = "1.0")
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Integer getCategoryNum(AdminCommunityBean adminCommunityBean) {
		return companyService.selectCompanyCountByCom(adminCommunityBean.getCommunityId());
	}

}
