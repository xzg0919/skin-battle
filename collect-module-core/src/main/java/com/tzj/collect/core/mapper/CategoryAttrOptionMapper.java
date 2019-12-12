package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.ali.AliCategoryAttrOptionBean;
import com.tzj.collect.entity.CategoryAttr;
import com.tzj.collect.entity.CategoryAttrOption;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryAttrOptionMapper  extends BaseMapper<CategoryAttrOption>{
	
	List<CategoryAttrOption> getDigitName(int id, int company);

	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @param   categoryAttrId : 分类属性Id
     * @param   companyId : 企业ID
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	List<CategoryAttrOption> getCategoryAttrOptionList(@Param("id") long categoryAttrId, @Param("companyId") long companyId);
	/**
	 * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
	 * @author 王灿
	 * @param   companyId : 企业ID
	 * @return List<CategoryAttrOption> : 有关所有的分类选项信息
	 */
	AliCategoryAttrOptionBean getCategoryAttrOptionById(@Param("id") long categoryAttrOptionId, @Param("companyId") long companyId);
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @param   categoryAttrId : 分类属性Id
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	List<CategoryAttrOption> getCategoryAttrOptionLists(@Param("id") long categoryAttrId);
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author sgmark@aliyun.com
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	List<CategoryAttr> getDigitNameRePlace(@Param("categoryId") int categoryId);

	List<CategoryAttrOption> getAppliceOrBigOption(@Param("title") String title);

	List<CategoryAttrOption> getOptionByCategoryAttrIdByCompanyId(@Param("categoryAttrId")long categoryAttrId,@Param("companyId")String companyId,@Param("cityId")Integer cityId);

}
