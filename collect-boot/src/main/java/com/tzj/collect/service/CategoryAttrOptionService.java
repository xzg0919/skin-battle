package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.param.AliCategoryAttrOptionBean;
import com.tzj.collect.api.business.param.CategoryBean;
import com.tzj.collect.entity.CategoryAttrOption;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CategoryAttrOptionService  extends IService<CategoryAttrOption>{
	
	/**
     * 根据分类选项的Id(主键)查询所有的分类选项信息
     * @author 王灿
     * @param   OptionId : 分类选项Id
     * @return CategoryAttrOption : 分类选项信息
     */
	CategoryAttrOption getOptionById(String OptionId);
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @param   categoryAttrId : 分类属性Id
     * @param   companyId : 企业ID
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	List<CategoryAttrOption> getOptionByCategoryAttrId(long categoryAttrId,long companyId);
	/**
	 * 根据分类属性选项的Id(categoryAttrId)查询有关所有的分类选项信息
	 * @author 王灿
	 * @param   categoryAttrOptionId : 分类属性选项Id
	 * @param   companyId : 企业ID
	 * @return List<CategoryAttrOption> : 有关所有的分类选项信息
	 */
	AliCategoryAttrOptionBean getCategoryAttrOptionById(long categoryAttrOptionId, long companyId);
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @param   categoryAttrId : 分类属性Id
     * @param   companyId : 企业ID
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	List<CategoryAttrOption> getOptionByCategoryAttrIds(long categoryAttrId);
	
	List<CategoryAttrOption> getDigitName(CategoryBean categoryBean); 
}
