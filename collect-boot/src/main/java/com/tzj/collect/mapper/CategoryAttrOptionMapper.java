package com.tzj.collect.mapper;

import java.util.List;

import com.tzj.collect.api.ali.param.AliCategoryAttrOptionBean;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CategoryAttr;
import com.tzj.collect.entity.CategoryAttrOption;

public interface CategoryAttrOptionMapper  extends BaseMapper<CategoryAttrOption>{
	
	List<CategoryAttrOption> getDigitName(int id,int company);
	
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @param   categoryAttrId : 分类属性Id
     * @param   companyId : 企业ID
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	List<CategoryAttrOption> getCategoryAttrOptionList(@Param("id")long categoryAttrId,@Param("companyId")long companyId);
	/**
	 * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
	 * @author 王灿
	 * @param   companyId : 企业ID
	 * @return List<CategoryAttrOption> : 有关所有的分类选项信息
	 */
	AliCategoryAttrOptionBean getCategoryAttrOptionById(@Param("id")long categoryAttrOptionId, @Param("companyId")long companyId);
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @param   categoryAttrId : 分类属性Id
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	List<CategoryAttrOption> getCategoryAttrOptionLists(@Param("id")long categoryAttrId);
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author zhangqiang
     * @param   categoryAttrId : 分类属性Id
     * @param   companyId : 企业ID
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	List<CategoryAttr> getDigitNameRePlace(@Param("categoryId")int categoryId);

}
