package com.tzj.collect.api.app;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.util.RecyclersUtils;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

/**
 * 分类接口列表
 * @author Michael_Wang
 *
 */
@ApiService
public class AppCategoryApi {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CompanyCategoryService priceService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private CompanyRecyclerService companyRecyclerService;
	@Autowired
	private RecyclersService recyclersService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private CompanyCategoryCityService companyCategoryCityService;
	/**
     * 取得所有一级分类 
     * @author wangcan
     * @return
     */
	 @Api(name = "app.category.getOneCategoryList", version = "1.0")
	 @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	 public List<Category> getOneCategoryList(CategoryBean categoryBean){
		 Order order = orderService.selectById(categoryBean.getOrderId());
		 Area area = areaService.selectById(order.getAreaId());
		 Serializable title = null;
	 	if (CategoryType.DIGITAL.toString().equals(categoryBean.getTitle())) {
	 		title = CategoryType.DIGITAL.getValue();
		}else if (CategoryType.HOUSEHOLD.toString().equals(categoryBean.getTitle())) {
			title = CategoryType.HOUSEHOLD.getValue();
		}
	 	//判断是否免费
	 	String isCash = categoryBean.getIsCash();
		 List<Category> categoryList = null;
		 if(StringUtils.isBlank(isCash)||"0".equals(isCash)) {
		 	//不免费
			 //判断是否配置该区域的城市价格
				categoryList = companyCategoryCityService.topListAppByCity(categoryBean.getLevel().toString(), title.toString(), order.getCompanyId().toString(), area.getParentId().toString());
			if(categoryList.isEmpty()){
				categoryList = categoryService.topListApp(categoryBean.getLevel(), title, RecyclersUtils.getRecycler().getId());
			}
	 	}else {
			 categoryList = categoryService.topListApps(categoryBean.getLevel(), title);
		 }
		 return categoryList;
	 }
	 /**
     * 根据一级分类id取得所有二级分类
     * @author wangcan 
     * @return
     */
	 @Api(name = "app.category.getTowCategoryList", version = "1.0")
	 @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	 public  Map<String, Object> getTowCategoryList(CategoryBean categoryBean){
//		 if (categoryBean.getTitle().equals(CategoryType.DIGITAL.name())) {
//			return categoryService.childList(categoryBean.getId());
//		}else if (categoryBean.getTitle().equals(CategoryType.HOUSEHOLD.name())) {
//			//return 
//		}
		return priceService.getTowCategoryList(categoryBean);
	 }

	 /**
	 * 取得六废的一级分类
	 * @author wangcan
	 * @return
	 */
	@Api(name = "app.category.getHouseOneCategoryList", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public List<Category> getHouseOneCategoryList(){
			//取得六废的一级分类
		return categoryService.topListApp(0, 2, RecyclersUtils.getRecycler().getId());
	}
	 /**
	 * 根据六废的一级分类取得二级分类
	 * @author wangcan
	 * @return
	 */
	@Api(name = "app.category.getHouseTwoCategoryList", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object getHouseTwoCategoryList(CategoryBean categoryBean){
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler());
		//根据回收人员ID查询所属企业
		CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recycler.getId()));
		List<ComCatePrice> comCatePriceList = companyCategoryService.getOwnnerPriceApp(categoryBean,companyRecycler.getCompanyId());
//		comCatePriceList.stream().forEach(comCatePrice -> {
//			comCatePrice.setPrice(comCatePrice.getPrice()*2);
//		});
		return comCatePriceList;
	}
}
