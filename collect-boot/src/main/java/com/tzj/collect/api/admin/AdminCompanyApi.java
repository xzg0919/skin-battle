package com.tzj.collect.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.common.util.SnUtils;
import com.tzj.collect.core.param.admin.AdminCommunityBean;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyCategory;
import com.tzj.collect.entity.CompanyServiceRange;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminCompanyApi {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private CompanyRecyclerService companyRecycleService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private CompanyServiceService companyServiceService;
	@Autowired
	private CommunityService communityService;
	

	/**
	 * 根据公司名称获取回收企业信息列表 @Title: getSearchCompany @Description: 【】 @date 2018年3月20日
	 * 上午11:28:00 @author:[王池][wjc2013481273@163.com] @param @param
	 * companybean @param @return 参数 @return Page<Company> 返回类型 @throws
	 */
	@Api(name = "admin.company.search", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Page<Company> getSearchCompany(CompanyBean companybean) {

		PageBean pageBean = new PageBean();//分页数据
		Page<Company> companyPage = companyService.getSearchCompany(companybean, pageBean);
		return companyPage;

	}

	/**
	 * 根据公司ID获取获取公司绑定回收人员个数 @Title: getRecycleCount @Description: 【】 @date
	 * 2018年3月20日 上午11:57:41 @author:[王池][wjc2013481273@163.com] @param @param
	 * companybean @param @return 参数 @return int 返回类型 @throws
	 */
	@Api(name = "admin.recycle.search", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public int getRecycleCount(CompanyBean companybean) {
		
		int count = companyRecycleService.selectRecycleByCompanyId(companybean.getId());
		return count;
	}

	/**
	 * 根据公司ID获取获取服务类型个数 @Title: getRecycleCount @Description: 【】 @date
	 * 2018年3月20日 上午11:57:41 @author:[王池][wjc2013481273@163.com] @param @param
	 * companybean @param @return 参数 @return int 返回类型 @throws
	 */
	@Api(name = "admin.category.search", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public int getCompanyCategoryCount(CompanyBean companybean) {
	
		 int count = companyCategoryService.selectCategoryByCompanyId(companybean.getId());
		return count;
	}

	/**
	 * 根据公司ID获取获取服务范围小区个数 @Title: getRecycleCount @Description: 【】 @date
	 * 2018年3月20日 上午11:57:41 @author:[王池][wjc2013481273@163.com] @param @param
	 * companybean @param @return 参数 @return int 返回类型 @throws
	 */
	@Api(name = "admin.companyService.search", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public int getCompanyServiceCount(CompanyBean companybean) {
		int count = 0;
		count = companyServiceService.selectCompanyServiceByCompanyId(companybean.getId());
		return count;
	}

	/**
	 * 新增回收公司 @Title: creatCompany @Description: 【】 @date 2018年3月21日
	 * 上午10:49:54 @author:[王池][wjc2013481273@163.com] @param @param companybean
	 * 参数 @return void 返回类型 @throws
	 */
	@Api(name = "admin.company.creat", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public String creatCompany(CompanyBean companybean) {
		String msg = "添加成功";
		try{
				//创建公司的一条记录
				Company company = new Company();
				company.setName(companybean.getCompanyName());
				company.setAddress(companybean.getAddress());
				company.setTel(companybean.getTelphone());
				company.setCompanyCode(SnUtils.generateSn());		
				company.setCreateDate(Calendar.getInstance().getTime());
				company.setUpdateDate(Calendar.getInstance().getTime());		
				companyService.insert(company);
				//添加回收企业分类关联记录				 
			   List<Integer> categoryIds = companybean.getCategory_id();
			for (int i = 0; i < categoryIds.size(); i++) {
				CompanyCategory companyCategory = new CompanyCategory();
				companyCategory.setCategoryId(categoryIds.get(i).toString());
				companyCategory.setCompany(company);
				companyCategory.setCompanyId(company.getId().toString());
				companyCategoryService.insert(companyCategory); 
			}
			    //添加回收企业服务范围记录
			   List<Integer> communityId = companybean.getCommunityId();
			for (int j = 0; j < communityId.size(); j++) {
				CompanyServiceRange companyServiceRange = new CompanyServiceRange();
				companyServiceRange.setCommunityId(communityId.get(j));
				companyServiceRange.setCompany(company);
				companyServiceRange.setCompanyId(company.getId().toString());
				companyServiceRange.setLevel("0");
				companyServiceRange.setAreaId(companybean.getStreetId());
				companyServiceRange.setParentIds(companybean.getCountyId()+"_"+companybean.getStreetId()+"_");			
				companyServiceService.insert(companyServiceRange);

			}

		}catch(Exception e){
			e.printStackTrace();
			msg="创建失败";
			return msg;
		}
		return msg;
	}

	
	/**
	 * 根据公司id获取服务范围的小区列表（点击查看）
	* @Title: getSelectedCommunityList
	* @Description: 【】
	* @date 2018年3月22日 上午9:18:49
	* @author:[王池]
	* @param @param companybean
	* @param @return    参数
	* @return List<Community>    返回类型
	* @throws
	 */
	@Api(name = "admin.company.selectedCommunity", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<AdminCommunityBean> getSelectedCommunityList(CompanyBean companybean){
		
		return communityService.getSelectedCommunityListByCompanyId(companybean.getId());
	}
	
	/**
	 * 新增公司时显示根据回收类型和服务范围过滤已被添加的小区的列表 @Title: getCommunityList @Description: 【】 @date
	 * 2018年3月21日 上午11:20:52 @author:[王池] @param @return 参数 @return(只传区县或者街道id,回收类型id)
	 * List<Community> 返回类型 @throws
	 */
	@Api(name = "admin.company.getCommunity", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<AdminCommunityBean> getCommunityList(CompanyBean companybean) {
        
		//公司可选择添加的服务范围列表
		List<AdminCommunityBean> communityList = communityService.selectCommunityByConditions(companybean);
		return communityList;
	}
	
	/**
	 * 编辑企业回收范围(传区县id和街道id,公司id,取消小区的id，要添加小区的id)
	* @Title: updateCommunityList                                              	
	* @Description: 【】
	* @date 2018年3月21日 下午5:21:00
	* @author:[王池][wjc2013481273@163.com]
	* @return void    返回类型
	* @throws
	 */
	@Api(name = "admin.company.updateCommunity", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public void updateCommunityList(CompanyBean companybean){
		//根据小区id取消该公司范围下的服务小区
		if(null!=companybean.getDelectCommunityId()){
			//根据id查找要取消的服务小区
			CompanyServiceRange companyServiceRange = companyServiceService.selectRangeByCommunityId(companybean.getDelectCommunityId());
			//修改关联表中该记录的状态使其不显示
			companyServiceRange.setDelFlag("1");
			companyServiceService.updateById(companyServiceRange);
         }
						
		if(null!=companybean.getInsertCommunityId()){
			CompanyServiceRange companyServiceRange = companyServiceService.selectOne(new EntityWrapper<CompanyServiceRange>().eq("community_id", companybean.getInsertCommunityId()));
		    //判断该服务小区在关联表中是否有记录
			if(null!=companyServiceRange){
				companyServiceRange.setDelFlag("0");
				companyServiceService.updateById(companyServiceRange);
			}else{
				//根据要添加的小区id添加企业服务范围记录
				CompanyServiceRange companyServiceRangeInsert = new CompanyServiceRange();
				companyServiceRangeInsert.setCommunityId(companybean.getInsertCommunityId());
				companyServiceRangeInsert.setCompanyId(companybean.getId().toString());
				companyServiceRangeInsert.setAreaId(companybean.getStreetId());
				companyServiceRangeInsert.setParentIds(companybean.getCountyId()+"_"+companybean.getStreetId()+"_");
				companyServiceRangeInsert.setLevel("0");
				companyServiceService.insert(companyServiceRangeInsert);
			}
		
								
		}
		
	}
	/**显示编辑企业回收服务范围列表
	 * 根据公司id,回收类型id,和区县或者街道id显示已过滤的小区(点击 编辑后显示的页面----区县或者街道id传一个)
	 */
	@Api(name = "admin.company.editorCommunity", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<AdminCommunityBean> getEditorCommunity(CompanyBean companybean){
		//根据公司id获取可添加的服务范围小区列表
		List<AdminCommunityBean> communityList =  communityService.getEditorCommunity(companybean);
		//根据公司id获取已选择的服务范围小区列表
		List<AdminCommunityBean> communitySelectedList = communityService.getSelectedCommunityListByCompanyId(companybean.getId());
		for(int i=0;i<communityList.size();i++){
			for(int j=0;j<communitySelectedList.size();j++){
				if(communitySelectedList.get(j).getCommunityId()==communityList.get(i).getCommunityId()){
					//给公司所选服务范围的小区添加标记
					communityList.get(i).setSelected("1");
				}
			}
		}
		return communityList;
	}

	// 获取所有的公司信息
	@Api(name = "admin.company.getAdminCompanyList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getAdminCompanyList(CompanyBean companyBean) {
		return companyService.getAdminCompanyList(companyBean.getCompanyName(),companyBean.getTitle(),companyBean.getPageBean());
	}

	// 删除的公司信息
	@Api(name = "admin.company.deleteCompanyById", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object deleteCompanyById(CompanyBean companyBean) {
		return companyService.deleteCompanyById(companyBean.getId().intValue());
	}

	// 根据公司id获取公司信息
	@Api(name = "admin.company.getCompanyDetail", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getCompanyDetail(CompanyBean companyBean) {
		return companyService.getCompanyDetail(companyBean.getId().intValue());
	}

	// 根据公司更新或新增信息
	@Api(name = "admin.company.updateCompanyDetail", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object updateCompanyDetail(CompanyBean companyBean) {
		return companyService.updateCompanyDetail(companyBean);
	}
	// 根据公司id获取公司服务范围信息
	@Api(name = "admin.company.adminCompanyRangeById", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object adminCompanyRangeById(CompanyBean companyBean) {
		return companyService.adminCompanyRangeById(companyBean.getId().intValue());
	}
	/**
	 * 获取各大公司的列表
	 * @param
	 * @return
	 */
	@Api(name = "admin.getCompanyList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getCompanyList(CompanyBean companyBean){
		return companyService.getCompanyList(companyBean.getCompanyName());
	}
	/**
	 * 根据公司ID和类型获取对应的城市
	 * @param
	 * @return
	 */
	@Api(name = "admin.getCityListByCompany", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public Object getCityListByCompany(CompanyBean companyBean){
		List<Map<String, Object>> companyRange = null;
		if ("1".equals(companyBean.getTitle())){
			companyRange = companyRecycleService.getAppliceCompanyRange(companyBean.getId().intValue());
		}else if ("2".equals(companyBean.getTitle())){
			companyRange = companyRecycleService.getHouseCompanyRange(companyBean.getId().intValue());
		}else if ("4".equals(companyBean.getTitle())){
			companyRange = companyRecycleService.getBigCompanyRange(companyBean.getId().intValue());
		}
		return companyRange;
	}


}
