package com.tzj.collect.api.business;

import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.core.param.business.CompanyBean;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.core.service.CategoryService;
import com.tzj.collect.core.service.CommunityService;
import com.tzj.collect.core.service.OrderLogService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

/**
 * 20180316
 * @author Can_Wang
 *
 */
@ApiService
public class DataBoard {
	
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private OrderService orderService;
	/**
	 * 数据看板的订单数据
	 * @author 王灿
	 * @return
	 * 
	*/
	 @Api(name = "business.dataBoard.orderData", version = "1.0")
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String, Object> getOrderData(CompanyBean companyBean) {
		 Map<String, Object> map = orderLogService.getOrderData(companyBean.getId(),companyBean.getStartTime());
		 return map;
	 }
	 /**
	 * 回收物明细
	 * @author 王灿
	 * @return
	 * 
	*/
	 @Api(name = "business.dataBoard.category", version = "1.0")
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<CategoryResult> getCategoryData(CompanyBean companyBean) {
		 List<CategoryResult> list = categoryService.getCategoryData(companyBean.getId());
		 return list;
	 }
	 /**
	 * 回收服务范围
	 * @author 王灿
	 * @return
	 * 
	*/
	 @Api(name = "business.dataBoard.community", version = "1.0")
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<Area> getCommunityData(CompanyBean companyBean) {
		 List<Area> list = communityService.getCommunityData(companyBean.getId());
		 return list;
	 }
	 
	 /**
	 * 数据看板折线图
	 * @author 王灿
	 * @return
	 * 
	*/
	 @Api(name = "business.dataBoard.brokenLine", version = "1.0")
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<Map<String,Object>> getBrokenLineData(CompanyBean companyBean) {
		 CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		 companyBean.setId(companyAccount.getCompanyId().toString());
		 List<Map<String,Object>> list = orderService.getBrokenLineData(companyBean);
		 return list;
	 }
}
