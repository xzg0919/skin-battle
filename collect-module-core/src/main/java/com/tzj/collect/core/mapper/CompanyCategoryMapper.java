package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.api.business.result.BusinessCategoryResult;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.param.business.ComIdAndCateOptIdBean;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 王灿
 **/
public interface CompanyCategoryMapper extends BaseMapper<CompanyCategory>{

	
	/**根据企业ID获取该企业的回收类型个数
	* @Title: selectCategoryByCompanyId
	* @Description: 【】
	* @date 2018年3月20日 下午12:02:26
	* @author:[王池][wjc2013481273@163.com]
	* @param @param id
	* @param @return    参数
	* @return int    返回类型
	* @throws
	*/
	
	int selectCategoryByCompanyId(long id);

	/**
	 * 获取所有父级id,name(如果服务小区个数为0,父级只有家电数码)
	 * @param companyId
	 * @return
	 */
	List<CategoryResult> getCategoryData(String companyId);
	
	/**
	 * 该地区有服务范围的公司,获取该公司的价格列表
	 * @return
	 */
	List<ComCatePrice> getOwnnerPrice(@Param("categoryBean") CategoryBean categoryBean, @Param("companyId") Integer companyId);

	List<ComCatePrice> getBigThingCategoryList(@Param("categoryId") Integer categoryId, @Param("companyId") Integer companyId, @Param("cityId") Integer cityId);

	/**
	 * 该地区有服务范围的公司,获取该公司的价格列表
	 * @return
	 */
	List<ComCatePrice> getOwnnerNoPrice(@Param("categoryBean") CategoryBean categoryBean, @Param("companyId") Integer companyId);


	List<ComCatePrice> getNoPrice(@Param("categoryBean") CategoryBean categoryBean, @Param("companyId") Integer companyId);

	/**
	 * 该地区有服务范围的公司,获取该公司的价格列表
	 * @return
	 */
	List<ComCatePrice> getOwnnerPriceApp(@Param("categoryBean") CategoryBean categoryBean, @Param("companyId") Integer companyId);
	/**
	 * 没在所有的公司服务范围,获取所有的平均价格
	 * @return
	 */
	List<ComCatePrice> getAvgPrice(@Param("categoryBean") CategoryBean categoryBean);



	/**
	 * 没在所有的公司服务范围,获取所有的平均价格
	 * @return
	 */
	List<ComCatePrice> getAvgNoPrice(@Param("categoryBean") CategoryBean categoryBean);
	/**
	 * 没在所有的公司服务范围,获取所有的平均价格
	 * @return
	 */
	List<ComCatePrice> getAvgPriceApp(@Param("categoryBean") CategoryBean categoryBean);

	List<ComCatePrice> getAppCategoryList();


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
	 * @return
	 */
	int updateHousePrice(CompanyCategory companyCategory);

	/**
	 * 添加关联表
	 */
	int insertPrice(CompanyCategory companyCategory);

	/**
	 * 得到变更后最多扣除的价格
	 * @param comIdAndCateOptIdBean
	 * @return
	 */
	String priceIsAvailable(ComIdAndCateOptIdBean comIdAndCateOptIdBean);

	CompanyCategory selectPriceByAttrId(@Param("id") String id, @Param("companyId") String companyId);
	/**
	 * 根据分类Id和小区Id查询所属企业
	 * @param categoryId : 分类Id
	 * @param communityId : 小区Id
	 * @return
	 */
	Company selectCompanys(@Param("categoryId") Integer categoryId, @Param("communityId") Integer communityId);

	Company selectCompanyByTitle(@Param("title") String title, @Param("communityId") Integer communityId);
}
