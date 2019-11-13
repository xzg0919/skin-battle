package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.param.business.ComIdAndCateOptIdBean;
import com.tzj.collect.core.result.business.BusinessCategoryResult;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author 王灿
 **/
public interface CompanyCategoryService extends IService<CompanyCategory>{

	
	/**根据企业ID获取该企业的回收类型个数
	* @Title: selectCategoryByCompanyId
	* @Description: 【】
	* @date 2018年3月20日 下午12:01:04
	* @author:[王池][wjc2013481273@163.com]
	* @param @param id
	* @param @return    参数
	* @return int    返回类型
	* @throws
	*/
	@DS("slave")
	int selectCategoryByCompanyId(long id);

	/**
	 * 该地区有服务范围的公司,获取该公司的价格列表
	 * @param companyId
	 * @return
	 */
	@DS("slave")
	List<ComCatePrice> getOwnnerPrice(@Param("categoryBean") CategoryBean categoryBean, @Param("companyId") Integer companyId);
	/**
	 * 没在所有的公司服务范围,获取所有的平均价格
	 * @return
	 */
	@DS("slave")
	List<ComCatePrice> getAvgPrice(@Param("categoryBean") CategoryBean categoryBean);
	@DS("slave")
	Map<String, Object> getPrice(CategoryBean categoryBean);
	@DS("slave")
	Map<String, Object> categoryTwoList(CategoryBean categoryBean);
	@DS("slave")
	Map<String,Object> categoryOneListToken(String aliUserId);
	@DS("slave")
	Map<String, Object> categoryHouseTwoList(CategoryBean categoryBean,String aliUserId);
	@DS("slave")
	Map<String, Object> getTowCategoryList(CategoryBean categoryBean);

	/**
	 * 根据公司id及分类属性选项id查找公司对应属性选项价格列表
	 * @param comIdAndCateOptIdBean
	 * @return
	 */
	@DS("slave")
	List<BusinessCategoryResult> selectComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean);


	/**
	 * 根据categoryId查询companyCategory表
	 * @param categoryId
	 * @return
	 */
	CompanyCategory selectByCategoryId(int categoryId);


	/**
	 * 更新价格
	 * @return
	 */
	int updatePrice(CompanyCategory companyCategory);

	/**
	 * 添加关联表
	 */
	int insertPrice(CompanyCategory companyCategory);

	CompanyCategory selectPriceByAttrId(String id, String companyId);
	@DS("slave")
	List<ComCatePrice> getOwnnerPriceApp(CategoryBean categoryBean, Integer companyId);
	@DS("slave")
	List<ComCatePrice> getOwnnerPriceApps(CategoryBean categoryBean, Integer companyId);
	/**
	 * 根据分类Id和小区Id查询所属企业
	 * @param categoryId : 分类Id
	 * @param communityId : 小区Id
	 * @return
	 */
	@DS("slave")
	Company selectCompanys(Integer categoryId, Integer communityId);
	/**
	 * 根据title和小区Id查询私海所属企业
	 * @param title : 类型
	 * @param communityId : 小区Id
	 * @return
	 */
	@DS("slave")
	Company selectCompanyByTitle(String title, Integer communityId);
}
