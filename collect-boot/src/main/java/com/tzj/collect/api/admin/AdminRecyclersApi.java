package com.tzj.collect.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.core.param.admin.RecyclersBean;
import com.tzj.collect.core.service.CompanyRecyclerService;
import com.tzj.collect.core.service.RecyclersService;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.Recyclers;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tzj.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * 
 * @ClassName: AdminRecyclersApi 
 * @Description: TODO
 * @author: 向忠国
 * @date: 2018年3月19日 下午2:03:44
 */
@ApiService
public class AdminRecyclersApi {
	
	@Autowired
	private RecyclersService recycleService;
	
	@Autowired
	private CompanyRecyclerService companyRecycleService;
	
	
	
	/**
	 * 
	 * @Title: getRecyclersPage 
	 * @Description:根据公司名称、回收人员名、回收人员ID返回回收人员列表 page
	 * @author: 向忠国
	 * @param @param bean
	 * @param @return   
	 * @return JSONObject   返回类型  
	 * @throw
	 */
	@Api(name = "recycler.getPage", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public JSONObject getRecyclersPage(RecyclersBean bean){
 		JSONObject obj=new JSONObject();
		List<Recyclers> recyclers= recycleService.getRecyclerPage(bean);
		int record =recycleService.getRecyclerPageSize(bean);
		int page=record%bean.getPage().getPageSize()==0?
				record/bean.getPage().getPageSize():record/bean.getPage().getPageSize()+1;
		for (Recyclers recyclers2 : recyclers) {
			List<Company> companyRecyclers=companyRecycleService.selectCompanyByRecyclerId(recyclers2.getId()+"");
			recyclers2.setCompanyCount(companyRecyclers.size());
			if(companyRecyclers.size()== 1){
				recyclers2.setCompanyName(companyRecyclers.get(0).getName());
			}
		}
		obj.put("records", recyclers);
		obj.put("pages", record==0?1:page);
		obj.put("size", bean.getPage().getPageSize());
		obj.put("total", recyclers.size());
		obj.put("current", bean.getPage().getPageNumber());
		return obj;
	}

	
	/**
	 * 回收人员入住的相关的回收企业
	 * 
	 */
	@Api(name = "recycler.companies", version = "1.0")
	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
	public List<Company> getRecyclerCompany(RecyclersBean recyclersBean) {
		List<Company> list = companyRecycleService.selectCompanyByRecyclerId(recyclersBean.getRecyclerId().toString());
		return list;
	}
	
//	/**
//	 * 根据回收人员id得到个人信息，评价数目
//	 * @author sgmark@aliyun.com
//	 * @param recyclersBean
//	 * @return
//	 */
//	@Api(name = "recycler.getrecbyid", version = "1.0")
//	@RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
//	public RecyclersBean getRecEvaById(RecyclersBean recyclersBean) {
//		RecyclersBean recycler = recycleService.getRecEvaById(recyclersBean.getRecyclerId().toString());
//		return recycler;
//	}
}
