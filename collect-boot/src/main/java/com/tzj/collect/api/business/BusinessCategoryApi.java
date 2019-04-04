package com.tzj.collect.api.business;

import com.taobao.api.ApiException;
import com.tzj.collect.api.business.param.CategoryAttrOptionBean;
import com.tzj.collect.api.business.param.CategoryBean;
import com.tzj.collect.api.business.param.ComIdAndCateOptIdBean;
import com.tzj.collect.api.business.result.BusinessCategoryResult;
import com.tzj.collect.api.business.result.CategoryResult;
import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.service.CategoryService;
import com.tzj.collect.service.CompanyCategoryAttrOptionService;
import com.tzj.collect.service.CompanyCategoryService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;
/**
 * 
 * @author Administrator
 *
 */
@ApiService
public class BusinessCategoryApi {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private CompanyCategoryAttrOptionService attrOptionService;

	 /**
     * 取得所有一级分类 
     * @param 
     * @return
     */
	@Api(name="business.category.toplist",version="1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String,List<Category>> getTopList(){
		Map<String,List<Category>> map = new HashMap<String, List<Category>>();
		CompanyAccount companyAccount = getCompanyAccount();
		int companyId = companyAccount.getCompanyId();
		List<Category> digitalList = categoryService.getTopList(companyId,CategoryType.DIGITAL.getValue());
		List<Category> houseHoldList = categoryService.getTopList(companyId,CategoryType.HOUSEHOLD.getValue());
		map.put("digitalList", digitalList);
		map.put("houseHoldList", houseHoldList);
		return map;
	}

	//大件一级分类接口
	@Api(name="business.category.bigtoplist",version="1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String,List<Category>> getTopBigthingList(){
		Map<String,List<Category>> map = new HashMap<>();
		CompanyAccount companyAccount = getCompanyAccount();
		int companyId = companyAccount.getCompanyId();
		map.put("bigthingList", categoryService.getTopList(companyId,CategoryType.BIGTHING.getValue()));
		return map;
	}

	
	/**
	 * 根据parentId查询家电数码二级菜单
	 * @param
	 * @return
	 */
	@Api(name="business.category.digitalsecondlist",version="1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<Category> getSecondList(CategoryBean categoryBean){
		String parentId = categoryBean.getParentId();
		return categoryService.getSecondList(parentId);
	}
	
	/**
	 * 查询家电数码详情
	 * @param 
	 * @return
	 */
	@Api(name="business.category.digitaldetail",version="1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String, Object> getDigitalDetail(CategoryBean categoryBean) throws ApiException{
		CompanyAccount companyAccount = getCompanyAccount();
		if (companyAccount != null) {
			return categoryService.getDigitalDetail(categoryBean, companyAccount.getCompanyId().toString());
		}else {
			throw new ApiException("请先登录,再执行更新操作");
		}
		
		//return categoryAttrOptionService.getDigitName(categoryBean);
	}
	
	/**
	 * 根据parentId查询生活垃圾品类、单价
	 * @param
	 * @return
	 */
	@Api(name="business.category.householddetail",version="1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<CategoryResult> getHouseHold(CategoryBean categoryBean){
		String parentId = categoryBean.getParentId();
		CompanyAccount companyAccount = getCompanyAccount();
		String companyId = companyAccount.getCompanyId().toString();
		return categoryService.getHouseHoldDetail(parentId, companyId);
	}
	
	/**
	 * 更新生活垃圾价格
	 * @param category
	 * @return
	 */
//	@Api(name="businesscategory.updateprice",version="1.0")
//	@SignIgnore
//	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
//	public String updatePrice(CompanyCategoryBean companyCategoryBean){
//		CompanyCategory newCompanyCategory = null;
//		boolean i = false;
//		//获取companyId
//		String companyId = getCompanyAccount().getCompanyId().toString();
//		//通过categoryId获取category对象
//		Category category = categoryService.selectById(companyCategoryBean.getCategoryId());
//		
//		if(CategoryType.DIGITAL.toString().equals(category.getTitle().toString())) {
//			//家用电器
//			
//			
//		}else if(CategoryType.HOUSEHOLD.toString().equals(category.getTitle().toString())) {
//			//生活垃圾
//			CompanyCategory companyCategory1 = companyCategoryService.selectByCategoryId(category.getId().intValue());
//			if(companyCategory1!=null) {
//				newCompanyCategory = new CompanyCategory();
//				newCompanyCategory.setPrice(Float.parseFloat(companyCategoryBean.getPrice()));
//				EntityWrapper<CompanyCategory> wrapper = new EntityWrapper<CompanyCategory>();
//				wrapper.eq("company_id", companyId);
//				wrapper.eq("category_id", companyCategoryBean.getCategoryId());
//				i =companyCategoryService.update(newCompanyCategory, wrapper);
//				//i = companyCategoryService.updatePrice(newCompanyCategory);
//				if(i) {
//					return "success";
//				}
//				return "fail";
//			}
//			newCompanyCategory = new CompanyCategory();
//			newCompanyCategory.setCategoryId(companyCategoryBean.getCategoryId());
//			newCompanyCategory.setParentId((Long.valueOf(category.getParentId())));
//			newCompanyCategory.setParentIds(category.getParentIds());
//			newCompanyCategory.setCompanyId(companyId);
//			newCompanyCategory.setPrice(Float.parseFloat(companyCategoryBean.getPrice()));
//			newCompanyCategory.setUnit("kg");
//			i = companyCategoryService.insertAllColumn(newCompanyCategory);
//			//i = companyCategoryService.insertPrice(newCompanyCategory);
//			if(i) {
//				return "success";
//			}
//			return "fail";
//		}
//		return "fail";
//	}
	/**
	 * 更新价格
	 * @param comIdAndCateOptIdBean
	 * @return
	 * @throws ApiException 
	 */
	@Api(name="business.category.updateprice",version="1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public boolean updatePrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws ApiException{
		CompanyAccount companyAccount = getCompanyAccount();
		if (companyAccount != null) {
			comIdAndCateOptIdBean.setCompanyId(companyAccount.getCompanyId().toString());
		}else {
			throw new ApiException("请先登录,再执行更新操作");
		}
		return categoryService.updatePrice(comIdAndCateOptIdBean);
	}
	
	public CompanyAccount  getCompanyAccount() {
		
		// 接口里面获取 CompanyAccount 的例子
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return companyAccount;
	}
	/**
	 * 根据公司id及分类属性选项id查找公司对	选项价格列表(家电数码)
	 * @param comIdAndCateOptIdBean
	 * @return
	 */
	@Api(name = "business.category.comcateoptprice", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<BusinessCategoryResult> selectComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) {
		comIdAndCateOptIdBean.setCompanyId(this.getCompanyAccount().getCompanyId().toString());
		return companyCategoryService.selectComCateAttOptPrice(comIdAndCateOptIdBean);
	}
	
	@Api(name = "business.category.modifycomoptprice", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public boolean modifyComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws ApiException {
		CompanyAccount companyAccount = getCompanyAccount();
		if (companyAccount != null) {
			comIdAndCateOptIdBean.setCompanyId(companyAccount.getCompanyId().toString());
		}else {
			throw new ApiException("请先登录,再执行更新操作");
		}
		return attrOptionService.modifyComCateAttOptPrice(comIdAndCateOptIdBean);
	}
	
	/**
	 * 提交价格集合
	 * @param
	 * @return
	 * @throws ApiException
	 */
	@Api(name = "business.category.updatecomcateattroptprice", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public boolean updateComCateAttrOptPrice(List<CategoryAttrOptionBean> categoryAttrOptionBeanList) throws ApiException {
		return attrOptionService.updateComCateAttOptPrice(categoryAttrOptionBeanList);
	}
}
