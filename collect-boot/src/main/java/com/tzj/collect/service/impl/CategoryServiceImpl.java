package com.tzj.collect.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.ali.param.CategoryAttrBean;
import com.tzj.collect.api.ali.result.ClassifyAndMoney;
import com.tzj.collect.api.business.param.CategoryBean;
import com.tzj.collect.api.business.param.ComIdAndCateOptIdBean;
import com.tzj.collect.api.business.result.CategoryResult;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.entity.CategoryAttr;
import com.tzj.collect.entity.CompanyCategory;
import com.tzj.collect.mapper.CategoryAttrOptionMapper;
import com.tzj.collect.mapper.CategoryMapper;
import com.tzj.collect.mapper.CompanyCategoryMapper;
import com.tzj.collect.service.CategoryService;
import com.tzj.collect.service.CompanyCategoryService;


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

	
	/**
	 * 获取一级类的商品
	 */
	@Override
	public List<Category> topList(int level, Serializable title) {
		EntityWrapper<Category> wraper = new EntityWrapper<Category>();
		wraper.eq("level_", level);
		wraper.eq("title", title);
		wraper.eq("unuseful","0");
		return this.selectList(wraper);
	}

	/**
	 * 获取一级类的商品
	 */
	@Override
	public List<Category> topListApp(int level, Serializable title) {
//		EntityWrapper<Category> wraper = new EntityWrapper<Category>();
//		wraper.eq("level_", level);
//		wraper.eq("title", title);
//		return this.selectList(wraper);
		return categoryMapper.topListApp(level,(int)title);
	}
	/**
	 * 获取一级类的商品
	 */
	@Override
	public List<Category> topListApps(int level, Serializable title) {
//		EntityWrapper<Category> wraper = new EntityWrapper<Category>();
//		wraper.eq("level_", level);
//		wraper.eq("title", title);
//		return this.selectList(wraper);
		return categoryMapper.topListApps(level,(int)title);
	}
	
	
	@Override
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
	public List<Category> getTopList(int companyId,Serializable title) {
		return categoryMapper.getTopList(companyId,title);
	}



	/**
	 * 根据parentId查询二级菜单
	 * @param parentId
	 * @return
	 */
	@Override
	public List<Category> getSecondList(String parentId) {
		return categoryMapper.getSecondList(parentId);
	}



	/**
	 * 根据parentId查询生活垃圾品类、单价
	 * @return
	 */
	@Override
	public List<CategoryResult> getHouseHoldDetail(String parentId,String companyId) {
		return categoryMapper.getHouseHoldDetail(parentId,companyId);
	}




	@Override
	@Transactional(readOnly=false)
	public boolean updatePrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) {
		if (comIdAndCateOptIdBean.getTitle().equals(CategoryType.HOUSEHOLD.name()) || comIdAndCateOptIdBean.getTitle().equals(CategoryType.DIGITAL.name())) {
			List<CategoryBean> priceList = comIdAndCateOptIdBean.getHouseholdPriceList();
			CompanyCategory companyCategory = null;
			for (CategoryBean categoryBean : priceList) {
				Category category = this.selectById(categoryBean.getId());
				if(category!=null) {
					EntityWrapper<CompanyCategory> wrapper = new EntityWrapper<CompanyCategory>();
					wrapper.eq("category_id", category.getId());
					wrapper.eq("company_id", comIdAndCateOptIdBean.getCompanyId());
					companyCategory = companyCategoryService.selectOne(wrapper);
				}
				//companyCategory = companyCategoryMapper.selectById(categoryBean.getId());
				if (companyCategory != null) {
					companyCategory.setPrice(Float.parseFloat(categoryBean.getPrice()));
					companyCategory.setUpdateDate(new Date());
					companyCategory.setUpdateBy(comIdAndCateOptIdBean.getCompanyId());
					//companyCategoryMapper.updateAllColumnById(companyCategory);
					EntityWrapper<CompanyCategory> wrapper = new EntityWrapper<CompanyCategory>();
					wrapper.eq("company_id", comIdAndCateOptIdBean.getCompanyId());
					wrapper.eq("category_id", categoryBean.getId());
					companyCategoryService.update(companyCategory, wrapper);
				}else{
					companyCategory = new CompanyCategory();
					if(category!=null) {
						companyCategory.setParentId(category.getParentId().longValue());
						companyCategory.setParentIds(category.getParentIds());
						companyCategory.setCategoryId(categoryBean.getId());
						companyCategory.setCompanyId(comIdAndCateOptIdBean.getCompanyId());
						companyCategory.setPrice(Float.parseFloat(categoryBean.getPrice()));
						//暂时将单位放进去（之后更新弃置todoing）
						companyCategory.setUnit("kg");
						companyCategory.setCreateBy(comIdAndCateOptIdBean.getCompanyId());
					}
						companyCategoryService.insertAllColumn(companyCategory);
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
	public Map<String, Object> getDigitalDetail(CategoryBean categoryBean, String companyId) {
		Map<String, Object> returnMap = new HashMap<>();
		List<CategoryAttr> attrOptions =  categoryAttrOptionMapper.getDigitNameRePlace(Integer.parseInt(categoryBean.getId()));
		ComIdAndCateOptIdBean comIdAndCateOptIdBean = null;
		if (attrOptions.size() > 0) {
			for (CategoryAttr categoryAttr : attrOptions) {
				comIdAndCateOptIdBean = new ComIdAndCateOptIdBean();
				comIdAndCateOptIdBean.setCateOptId(categoryAttr.getId().toString());
				comIdAndCateOptIdBean.setCompanyId(companyId);
				categoryAttr.setResultList(companyCategoryService.selectComCateAttOptPrice(comIdAndCateOptIdBean));
			}
		}
		//获取当前公司回收价格
		returnMap.put("List", attrOptions);
		EntityWrapper<CompanyCategory> wraper = new EntityWrapper<CompanyCategory>();
		wraper.eq("company_id", comIdAndCateOptIdBean.getCompanyId());
		wraper.eq("category_id", categoryBean.getId().toString());
		wraper.eq("del_flag", "0");
		CompanyCategory attrOption = companyCategoryService.selectOne(wraper);
		if (attrOption == null) {
			returnMap.put("Price", BigDecimal.ZERO);
		}else{
			returnMap.put("Price", attrOption.getPrice());
		}
		return returnMap;
	}




}
