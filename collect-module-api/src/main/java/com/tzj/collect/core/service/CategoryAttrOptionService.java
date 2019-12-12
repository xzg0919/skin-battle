package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.AliCategoryAttrOptionBean;
import com.tzj.collect.core.param.business.CategoryBean;
import com.tzj.collect.entity.CategoryAttrOption;

import java.util.List;

public interface CategoryAttrOptionService  extends IService<CategoryAttrOption>{
	
	/**
     * 根据分类选项的Id(主键)查询所有的分类选项信息
     * @author 王灿
     * @param   OptionId : 分类选项Id
     * @return CategoryAttrOption : 分类选项信息
     */
	@DS("slave")
	CategoryAttrOption getOptionById(String OptionId);
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @param   categoryAttrId : 分类属性Id
     * @param   companyId : 企业ID
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	@DS("slave")
	List<CategoryAttrOption> getOptionByCategoryAttrId(long categoryAttrId, long companyId);
	/**
	 * 根据分类属性选项的Id(categoryAttrId)查询有关所有的分类选项信息
	 * @author 王灿
	 * @param   categoryAttrOptionId : 分类属性选项Id
	 * @param   companyId : 企业ID
	 * @return List<CategoryAttrOption> : 有关所有的分类选项信息
	 */
	@DS("slave")
	AliCategoryAttrOptionBean getCategoryAttrOptionById(long categoryAttrOptionId, long companyId);
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @param   categoryAttrId : 分类属性Id
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	@DS("slave")
	List<CategoryAttrOption> getOptionByCategoryAttrIds(long categoryAttrId);
	@DS("slave")
	List<CategoryAttrOption> getDigitName(CategoryBean categoryBean);
	@DS("slave")
	List<CategoryAttrOption> getAppliceOrBigOption(String title);
	@DS("slave")
	List<CategoryAttrOption> getOptionByCategoryAttrIdByCompanyId(long categoryAttrId,String companyId,Integer cityId);
}
