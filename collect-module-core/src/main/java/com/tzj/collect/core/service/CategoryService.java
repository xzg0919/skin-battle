package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.core.param.ali.CategoryAttrBean;
import com.tzj.collect.core.result.ali.ClassifyAndMoney;
import com.tzj.collect.core.param.business.CategoryBean;
import com.tzj.collect.core.param.business.ComIdAndCateOptIdBean;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.entity.Category;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface CategoryService extends IService<Category> {

	
	/**
	* @Title: getCategoryListByLevel
	* @Description: 【】
	* @date 2018年3月6日 下午3:07:20
	* @author:[王池][wjc2013481273@163.com]
	* @param @param level
	* @param @return    参数
	* @return List<Category>    返回类型
	* @throws
	*/
	@DS("slave")
	List<Category> topList(int level, Serializable title, String isFiveKg);
	/**
	* @Title: getCategoryListByLevel
	* @Description: 【】
	* @date 2018年3月6日 下午3:07:20
	* @author:[王池][wjc2013481273@163.com]
	* @param @param level
	* @param @return    参数
	* @return List<Category>    返回类型
	* @throws
	*/
	@DS("slave")
	List<Category> topListApp(int level, Serializable title, Long recId);
	/**
	* @Title: getCategoryListByLevel
	* @Description: 【】
	* @date 2018年3月6日 下午3:07:20
	* @author:[王池][wjc2013481273@163.com]
	* @param @param level
	* @param @return    参数
	* @return List<Category>    返回类型
	* @throws
	*/
	@DS("slave")
	List<Category> topListApps(int level, Serializable title);


	/**
	* @Title: getSecondCategoryListById
	* @Description: 【】
	* @date 2018年3月6日 下午4:01:30
	* @author:[王池][wjc2013481273@163.com]
	* @param @param id
	* @param @return    参数
	* @return List<Category>    返回类型
	* @throws
	*/
	@DS("slave")
	List<Category> childList(Integer id);


	/**
     * 根据分类Id（主键）查询分类信息
     * @author 王灿
     * @param   categoryId : 分类Id
     * @return Category : 分类信息
     */
	@DS("slave")
	Category getCategoryById(long categoryId);

	/**
	 * 根据分类id查询对应的分类信息和对应的分类属性条数
	 * @author 王灿
     * @param   categoryId : 分类Id
	 */
	@DS("slave")
	Category selectListCategory(String categoryId);

	 /**
     * 根据分类Id删除分类
     * @author: 王灿
     * @param  categoryId : 分类Id
     * @return TokenBean    返回类型
     */
	boolean deleteByCategoryId(String categoryId);
	 /**
	 * 回收物明细
	 * @author 王灿
	 * @param companyId:企业Id
	 * @return
	 *
	*/
	 @DS("slave")
	List<CategoryResult> getCategoryData(String companyId);


	ClassifyAndMoney reckon(CategoryAttrBean categoryAttrBean);

	/**
	 * 根据companyid查询一级菜单
	 * @param companyId
	 * @return
	 */
	@DS("slave")
	List<Category> getTopList(int companyId, Serializable title);

	/**
	 * 根据parentId查询二级菜单
	 * @param parentId
	 * @return
	 */
	@DS("slave")
	List<Category> getSecondList(String parentId);

	/**
	 * 根据parentId查询生活垃圾品类、单价
	 * @param parentId
	 * @return
	 */
	@DS("slave")
	List<CategoryResult> getHouseHoldDetail(String parentId, String companyId, String cityId);


	boolean updatePrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws ApiException;
	/**
	 * 获取分类属性所有价格及当前价格
	 * @param categoryBean
	 * @param string
	 * @return
	 */
	@DS("slave")
	Map<String, Object> getDigitalDetail(CategoryBean categoryBean, String string);

	@DS("slave")
	List<Map<String,Object>> getIsOpenCategory(String companyId);
}
