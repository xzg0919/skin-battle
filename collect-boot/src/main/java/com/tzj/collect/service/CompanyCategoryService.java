package com.tzj.collect.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.result.ComCatePrice;
import com.tzj.collect.api.business.param.ComIdAndCateOptIdBean;
import com.tzj.collect.api.business.result.BusinessCategoryResult;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyCategory;

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
	
	int selectCategoryByCompanyId(long id);

	/**
	 * 该地区有服务范围的公司,获取该公司的价格列表
	 * @param orderbean
	 * @param companyId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	List<ComCatePrice> getOwnnerPrice(@Param("categoryBean")CategoryBean categoryBean, @Param("companyId")Integer companyId);
	/**
	 * 没在所有的公司服务范围,获取所有的平均价格
	 * @param orderbean
	 * @param companyId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	List<ComCatePrice> getAvgPrice(@Param("categoryBean")CategoryBean categoryBean);

	Map<String, Object> getPrice(CategoryBean categoryBean);

	Map<String, Object> categoryTwoList(CategoryBean categoryBean);

	Map<String, Object> categoryHouseTwoList(CategoryBean categoryBean);
	
	Map<String, Object> getTowCategoryList(CategoryBean categoryBean);

	/**
	 * 根据公司id及分类属性选项id查找公司对应属性选项价格列表
	 * @param comIdAndCateOptIdBean
	 * @return
	 */
	List<BusinessCategoryResult> selectComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean);

	
	/**
	 * 根据categoryId查询companyCategory表
	 * @param categoryId
	 * @return
	 */
	CompanyCategory selectByCategoryId(int categoryId);


	/**
	 * 更新价格
	 * @param category
	 * @return
	 */
	int updatePrice(CompanyCategory companyCategory);
	
	/**
	 * 添加关联表
	 */
	int insertPrice(CompanyCategory companyCategory);
	
	CompanyCategory selectPriceByAttrId(String id, String companyId);
	
	List<ComCatePrice> getOwnnerPriceApp(CategoryBean categoryBean, Integer companyId);
	/**
	 * 根据分类Id和小区Id查询所属企业
	 * @param categoryId : 分类Id
	 * @param communityId : 小区Id
	 * @return
	 */
	Company selectCompany(Integer categoryId,Integer communityId);
}
