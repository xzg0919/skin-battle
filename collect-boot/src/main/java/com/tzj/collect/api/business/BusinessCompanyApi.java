package com.tzj.collect.api.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.api.admin.param.RecyclersBean;
import com.tzj.collect.api.business.param.CompanyAccountBean;
import com.tzj.collect.api.business.result.BusinessRecType;
import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.entity.CompanyRecycler;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.service.AreaService;
import com.tzj.collect.service.CompanyRecyclerService;
import com.tzj.collect.service.CompanyService;
import com.tzj.collect.service.RecyclersService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	/**
	 * 根据公司id查询回收人员Page（可传<回收人员身份证或姓名>）
	 * 
	 * @param companyAccountBean
	 * @return
	 */
	@Api(name = "business.company.search", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	@SignIgnore
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
	 * @author zhangqiang
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
	 * @author zhangqiang
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
	 * @author zhangqiang
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
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getCompanyRange() {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return comRecService.getCompanyRange(companyAccount.getCompanyId());
	}
	/**
	 * 根据市级Id获取市级下属行政区
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.company.getAreasById", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getAreasById(CompanyBean companyBean) {
		List<Map<String,Object>> resultList = new ArrayList<>();
		List<Area> areaList = areaService.selectList(new EntityWrapper<Area>().eq("parent_id", companyBean.getAreaId()));
		for (Area area: areaList) {
			Map<String,Object> resultMap = new HashMap<>();
			List<Area> streeList = areaService.selectList(new EntityWrapper<Area>().eq("parent_id", area.getId()));
			resultMap.put("streeList",streeList);
			resultMap.put("areaName",area.getAreaName());
			resultMap.put("id",area.getId());
			resultList.add(resultMap);
		}
		return resultList;
	}
}
