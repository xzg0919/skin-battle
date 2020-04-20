package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.xianyu.XyCategory;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CategoryMapper extends BaseMapper<Category>  {

	/**
	 * 根据分类id查询对应的分类信息和对应的分类属性条数
	 * @author 王灿
     * @param   categoryId : 分类Id
	 */
	Category selectListCategory(String categoryId);
	
	/**
	 * 根据companyid查询一级菜单
	 * @param companyId
	 * @return
	 */
	List<Category> getTopList(int companyId, Serializable title);


	/**
	 * 根据parentId查询二级菜单
	 * @param parentId
	 * @return
	 */
	List<Category> getSecondList(String parentId);

	/**
	 * 根据parentId查询生活垃圾品类、单价
	 * @param parentId
	 * @return
	 */
	List<CategoryResult> getHouseHoldDetail(String parentId, String companyId);
	/**
	 * 获取一级分类，保证价格不为0 或无价值的分类
	 * @param level
	 * @param title
	 * @return
	 */
	List<Category> topListApp(@Param("level") Integer level, @Param("title") Integer title, @Param("recId") Long recId);
	/**
	 * 获取一级分类，保证价格不为0 或无价值的分类
	 * @param level
	 * @param title
	 * @return
	 */
	List<Category> topListApps(@Param("level") Integer level, @Param("title") Integer title);

	List<Map<String,Object>> getIsOpenCategory(String companyId);

	BigDecimal selectCategoryPriceByCityCompanyId(@Param("categoryId") Long categoryId, @Param("cityId")Integer cityId, @Param("companyId")String companyId);

	List<Map<String,Object>> selectXyList();

}
