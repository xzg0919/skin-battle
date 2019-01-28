package com.tzj.collect.api.app;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;
import static com.tzj.collect.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.common.util.RecyclersUtils;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.entity.CompanyRecycler;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.service.CategoryService;
import com.tzj.collect.service.CompanyCategoryService;
import com.tzj.collect.service.CompanyRecyclerService;
import com.tzj.collect.service.RecyclersService;
import com.tzj.collect.service.impl.CompanyCategoryServiceImpl;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

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
	/**
     * 取得所有一级分类 
     * @author wangcan
     * @param title : 分类属性
     * @param level : 分类级别
     * @return
     */
	 @Api(name = "app.category.getOneCategoryList", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	 public List<Category> getOneCategoryList(CategoryBean categoryBean){
		Serializable title = null;
	 	if (CategoryType.DIGITAL.toString().equals(categoryBean.getTitle())) {
	 		title = CategoryType.DIGITAL.getValue();
		}else if (CategoryType.HOUSEHOLD.toString().equals(categoryBean.getTitle())) {
			title = CategoryType.HOUSEHOLD.getValue();
		}
	 	//判断是否免费
	 	String isCash = categoryBean.getIsCash();
	 	if(StringUtils.isBlank(isCash)||"0".equals(isCash)) {
	 		//不免费
	 		return categoryService.topListApp(categoryBean.getLevel(), title);		 				 
	 	}else {
	 		return categoryService.topListApps(categoryBean.getLevel(), title);
	 	}
	 }
	 /**
     * 根据一级分类id取得所有二级分类
     * @author wangcan 
     * @param  communityId : 小区Id
     * @param 	id : 一级分类的主键
     * @return
     */
	 @Api(name = "app.category.getTowCategoryList", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	 public  Map<String, Object> getTowCategoryList(CategoryBean categoryBean){
//		 if (categoryBean.getTitle().equals(CategoryType.DIGITAL.name())) {
//			return categoryService.childList(categoryBean.getId());
//		}else if (categoryBean.getTitle().equals(CategoryType.HOUSEHOLD.name())) {
//			//return 
//		}
		return priceService.getTowCategoryList(categoryBean);
	 }
	 public static void main(String[] args) {
		System.out.println(CategoryType.DIGITAL.toString());
	}
	 
	 /**
	 * 取得六废的一级分类
	 * @author wangcan
	 * @return
	 */
	@Api(name = "app.category.getHouseOneCategoryList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public List<Category> getHouseOneCategoryList(){
			//取得六废的一级分类
		return categoryService.topListApp(0, 2);
	}
	 /**
	 * 根据六废的一级分类取得二级分类
	 * @author wangcan
	 * @param title : 分类属性
	 * @param level : 分类级别
	 * @return
	 */
	@Api(name = "app.category.getHouseTwoCategoryList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object getHouseTwoCategoryList(CategoryBean categoryBean){
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler());
		//根据回收人员ID查询所属企业
		CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recycler.getId()));
		return companyCategoryService.getOwnnerPriceApp(categoryBean,companyRecycler.getCompanyId());
	}
}
