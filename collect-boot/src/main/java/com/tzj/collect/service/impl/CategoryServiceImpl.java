package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.ali.param.CategoryAttrBean;
import com.tzj.collect.api.ali.result.ClassifyAndMoney;
import com.tzj.collect.api.business.param.CategoryBean;
import com.tzj.collect.api.business.param.ComIdAndCateOptIdBean;
import com.tzj.collect.api.business.result.ApiUtils;
import com.tzj.collect.api.business.result.BusinessCategoryResult;
import com.tzj.collect.api.business.result.CategoryResult;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.entity.CategoryAttr;
import com.tzj.collect.entity.CompanyCategory;
import com.tzj.collect.entity.CompanyCategoryCity;
import com.tzj.collect.mapper.CategoryAttrOptionMapper;
import com.tzj.collect.mapper.CategoryMapper;
import com.tzj.collect.mapper.CompanyCategoryMapper;
import com.tzj.collect.service.CategoryService;
import com.tzj.collect.service.CompanyCategoryAttrOptionCityService;
import com.tzj.collect.service.CompanyCategoryCityService;
import com.tzj.collect.service.CompanyCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;


@Service
@Transactional(readOnly=true)
public class CategoryServiceImpl  extends  ServiceImpl< CategoryMapper, Category> implements CategoryService{
    
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private CompanyCategoryMapper companyCategoryMapper;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private CategoryAttrOptionMapper categoryAttrOptionMapper;
	@Autowired
	private CompanyCategoryAttrOptionCityService companyCategoryAttrOptionCityService;
	@Autowired
	private CompanyCategoryCityService companyCategoryCityService;

	
	/**
	 * 获取一级类的商品
	 */
	@Override
	@DS("slave")
	public List<Category> topList(int level, Serializable title,String isFiveKg) {
		EntityWrapper<Category> wraper = new EntityWrapper<Category>();
		wraper.eq("level_", level);
		wraper.eq("title", title);
		wraper.eq("unuseful","0");
		wraper.orderBy("create_date",false);
		if ("Y".equals(isFiveKg)){
		}
		return this.selectList(wraper);
	}

	/**
	 * 获取一级类的商品
	 */
	@Override
	@DS("slave")
	public List<Category> topListApp(int level, Serializable title, Long recId) {
//		EntityWrapper<Category> wraper = new EntityWrapper<Category>();
//		wraper.eq("level_", level);
//		wraper.eq("title", title);
//		return this.selectList(wraper);
		System.out.println(recId);
		return categoryMapper.topListApp(level,(int)title, recId);
	}
	/**
	 * 获取一级类的商品
	 */
	@Override
	@DS("slave")
	public List<Category> topListApps(int level, Serializable title) {
//		EntityWrapper<Category> wraper = new EntityWrapper<Category>();
//		wraper.eq("level_", level);
//		wraper.eq("title", title);
//		return this.selectList(wraper);
		return categoryMapper.topListApps(level,(int)title);
	}
	
	
	@Override
	@DS("slave")
	public List<Category> childList(Integer id) {
		EntityWrapper<Category> wraper = new EntityWrapper<Category>();
		wraper.eq("parent_id", id);
		return this.selectList(wraper);
	}




	/**
     * 根据分类Id（主键）查询分类信息
     * @author 王灿
     * @param   categoryId : 分类Id
     * @return Category : 分类信息
     */
	@Override
	@DS("slave")
	public Category getCategoryById(long categoryId) {
		EntityWrapper<Category> wrapper = new EntityWrapper<Category>();
		wrapper.eq("id", categoryId);
		wrapper.eq("del_flag", "0");
		return this.selectOne(wrapper);
	}



	/**
	 * 根据分类id查询对应的分类信息和对应的分类属性条数
	 * @author 王灿
     * @param   categoryId : 分类Id
	 */
	@Override
	@DS("slave")
	public Category selectListCategory(String categoryId) {
		return categoryMapper.selectListCategory(categoryId);
	}



	
	 /**
     * 根据分类Id删除分类
     * @author: 王灿
     * @param  categoryId : 分类Id
     * @return TokenBean    返回类型  
     */
	@Transactional
	@Override
	public boolean deleteByCategoryId(String categoryId) {
		Category category = this.selectById(categoryId);
		category.setDelFlag("1");
		return this.updateById(category);
	}



	 /**
	 * 回收物明细
	 * @author 王灿
	 * @param companyId:企业Id
	 * @return 
	 * 
	 */
	@Override
	@DS("slave")
	public List<CategoryResult> getCategoryData(String companyId) {
		List<Category> categoryChildList = null;
		Set<String> cateChildName = null;
		List<CategoryResult> list = companyCategoryMapper.getCategoryData(companyId);
		if(!list.isEmpty()) {
			for (CategoryResult categoryResult : list) {
				cateChildName = new HashSet<>();
				//根据父类id获取categoryChildList
				categoryChildList = this.childList(Integer.parseInt(categoryResult.getId()));
				for (Category categoryChild : categoryChildList) {
					cateChildName.add(categoryChild.getName());
				}
				categoryResult.setChildName(cateChildName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
			}
		}
		return list;
	}




	/**
	 * 计算价格
	 */
	@Override
	public ClassifyAndMoney reckon(CategoryAttrBean categoryAttrBean) {
		//分类Id
		long categoryId = categoryAttrBean.getCategoryId();
		//根据分类Id查询分类信息
		Category category =this.getCategoryById(categoryId);
		Category parent = this.getCategoryById(category.getParentId());
		BigDecimal price = category.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
		ClassifyAndMoney classifyAndMoney = new ClassifyAndMoney();
		Integer weight = categoryAttrBean.getWeight();
		if (weight > 0) {
			classifyAndMoney.setMoney(price.multiply(new BigDecimal(categoryAttrBean.getWeight())));
		}
		StringBuilder builder = new StringBuilder();
		builder.append(parent.getName());
		builder.append("-");
		builder.append(category.getName());
		classifyAndMoney.setClassify(builder.toString());
		return classifyAndMoney;
	}



	/**
	 * 根据companyid查询一级菜单
	 * @param companyId
	 * @return
	 */
	@Override
	@DS("slave")
	public List<Category> getTopList(int companyId,Serializable title) {
		return categoryMapper.getTopList(companyId,title);
	}



	/**
	 * 根据parentId查询二级菜单
	 * @param parentId
	 * @return
	 */
	@Override
	@DS("slave")
	public List<Category> getSecondList(String parentId) {
		return categoryMapper.getSecondList(parentId);
	}



	/**
	 * 根据parentId查询生活垃圾品类、单价
	 * @return
	 */
	@Override
	@DS("slave")
	public List<CategoryResult> getHouseHoldDetail(String parentId,String companyId,String cityId) {
		List<CategoryResult> categoryResultList = null;
		categoryResultList = companyCategoryCityService.getCityHouseHoldDetail(parentId,companyId,cityId);
		if(categoryResultList.isEmpty()){
			categoryResultList = categoryMapper.getHouseHoldDetail(parentId,companyId);
		}
		return categoryResultList;
	}




	@Override
	@Transactional(readOnly=false)
	public boolean updatePrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws ApiException {
		if (comIdAndCateOptIdBean.getTitle().equals(CategoryType.HOUSEHOLD.name()) || comIdAndCateOptIdBean.getTitle().equals(CategoryType.DIGITAL.name()) || comIdAndCateOptIdBean.getTitle().equals(CategoryType.BIGTHING.name())) {
			List<CategoryBean> priceList = comIdAndCateOptIdBean.getHouseholdPriceList();
			for (CategoryBean categoryBean : priceList) {
				Category category = this.selectById(categoryBean.getId());
				if(category==null) {
					throw  new ApiException("传入的分类Id不存在："+categoryBean.getId());
				}
				CompanyCategoryCity companyCategoryCity = companyCategoryCityService.selectOne(new EntityWrapper<CompanyCategoryCity>().eq("city_id", comIdAndCateOptIdBean.getCityId()).eq("company_id", comIdAndCateOptIdBean.getCompanyId()).eq("category_id", categoryBean.getId()).eq("del_flag", 0));
				if (companyCategoryCity != null) {
					companyCategoryCity.setPrice(new BigDecimal(categoryBean.getPrice()));
					companyCategoryCity.setUpdateDate(new Date());
					companyCategoryCity.setCityId(comIdAndCateOptIdBean.getCityId());
					companyCategoryCity.setUpdateBy(comIdAndCateOptIdBean.getCompanyId());
					//companyCategoryMapper.updateAllColumnById(companyCategory);
					companyCategoryCityService.updateById(companyCategoryCity);
				}else{
					companyCategoryCity = new CompanyCategoryCity();
					if(category!=null) {
						companyCategoryCity.setParentId(category.getParentId());
						companyCategoryCity.setParentIds(category.getParentIds());
						companyCategoryCity.setCategoryId(Integer.parseInt(categoryBean.getId()));
						companyCategoryCity.setCompanyId(comIdAndCateOptIdBean.getCompanyId());
						companyCategoryCity.setPrice(new BigDecimal(categoryBean.getPrice()));
						//暂时将单位放进去（之后更新弃置todoing）
						companyCategoryCity.setUnit("kg");
						companyCategoryCity.setCityId(comIdAndCateOptIdBean.getCityId());
						companyCategoryCity.setCreateBy(comIdAndCateOptIdBean.getCompanyId());
					}
					companyCategoryCityService.insert(companyCategoryCity);
					//companyCategoryMapper.insert(companyCategory);
				}
			}
			return true;
		}else {
			try {
				throw new ApiException("修改的总分类不存在，请重新输入");
			} catch (ApiException e) {
				e.getCause();
			}
		}
		return false;
	}

	@Override
	@DS("slave")
	public Map<String, Object> getDigitalDetail(CategoryBean categoryBean, String companyId) {
		Map<String, Object> returnMap = new HashMap<>();
		List<CategoryAttr> attrOptions =  categoryAttrOptionMapper.getDigitNameRePlace(Integer.parseInt(categoryBean.getId()));
		ComIdAndCateOptIdBean comIdAndCateOptIdBean = null;
		List<BusinessCategoryResult> businessCategoryResults = null;
		if (!attrOptions.isEmpty()) {
			for (CategoryAttr categoryAttr : attrOptions) {
				comIdAndCateOptIdBean = new ComIdAndCateOptIdBean();
				comIdAndCateOptIdBean.setCateOptId(categoryAttr.getId().toString());
				comIdAndCateOptIdBean.setCompanyId(companyId);
				businessCategoryResults = companyCategoryAttrOptionCityService.selectComCityCateAttOptPrice(categoryBean.getCityId(), companyId, categoryAttr.getId().toString());
				businessCategoryResults = getBusinessCategoryResult(businessCategoryResults);
				if (businessCategoryResults.isEmpty()) {
					businessCategoryResults = companyCategoryService.selectComCateAttOptPrice(comIdAndCateOptIdBean);
				}
				categoryAttr.setResultList(businessCategoryResults);
			}
		}
		returnMap.put("List", attrOptions);
		//获取当前公司回收价格
		CompanyCategoryCity companyCategoryCity = companyCategoryCityService.selectOne(new EntityWrapper<CompanyCategoryCity>().eq("company_id", comIdAndCateOptIdBean.getCompanyId()).eq("category_id", categoryBean.getId()).eq("del_flag", "0").eq("city_id", categoryBean.getCityId()));
		if (companyCategoryCity == null) {
			CompanyCategory attrOption = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id", comIdAndCateOptIdBean.getCompanyId()).eq("category_id", categoryBean.getId()).eq("del_flag", "0"));
			if(null != attrOption){
				returnMap.put("Price", attrOption.getPrice());
			}else {
				returnMap.put("Price", BigDecimal.ZERO);
			}
		}else{
			returnMap.put("Price", companyCategoryCity.getPrice());
		}
		return returnMap;
	}

	public List<BusinessCategoryResult> getBusinessCategoryResult(List<BusinessCategoryResult> businessCategoryResultsList){
		String price = null;
		for (BusinessCategoryResult businessCategoryResult : businessCategoryResultsList) {
			if (businessCategoryResult.getComOptPrice() != null) {
				price = businessCategoryResult.getComOptPrice();
			}
			if (price != null) {
				if (new BigDecimal(price).compareTo(BigDecimal.ZERO) >= 0) {
					businessCategoryResult.setComOptAddPrice(price);
				}else{
					businessCategoryResult.setComOptDecprice(new BigDecimal(price).abs().toString());
				}
			}
		}
		return businessCategoryResultsList;
	}




}
