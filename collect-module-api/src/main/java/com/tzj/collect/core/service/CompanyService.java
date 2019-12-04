package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.iot.IotCompanyResult;
import com.tzj.collect.core.result.business.BusinessRecType;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyAccount;

import java.util.List;

public interface CompanyService  extends IService<Company>{

	
	/**
	* @Title: getSearchCompany
	* @Description: 【】
	* @date 2018年3月19日 下午6:04:37
	* @author:[王池][wjc2013481273@163.com]
	* @param @param companybean
	* @param @param pageBean
	* @param @return    参数
	* @return Page<Company>    返回类型
	* @throws
	*/
	@DS("slave")
	Page<Company> getSearchCompany(CompanyBean companybean, PageBean pageBean);

	

	/**
	 * @author sgmark@aliyun.com
	 * 根据小区Id取得回收企业总数
	 * @param comId
	 * @return
	 */
	@DS("slave")
	Integer selectCompanyCountByCom(String comId);
	/**
	 * @author sgmark@aliyun.com
	 * 根据小区Id取得回收企业回收垃圾类型（去重）
	 * @param comId
	 * @return
	 */
	@DS("slave")
	Integer selectCategoryCountByCom(String comId);
	/**
	 * 根据小区id取得服务公司列表
	 * @author sgmark@aliyun.com
	 * @return
	 */
	@DS("slave")
	List<Company> selectCompanyListByComm(String commId);

	/**
	 * 根据公司id返回公司回收类型
	 * @param comId
	 * @return
	 */
	@DS("slave")
	List<BusinessRecType> getTypeByComId(String comId); 
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业Id
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	@DS("slave")
	Integer getCompanyIdByIds(Integer CommunityId, Integer CategoryId);

	/**
	 * 根据公司账号获取当前公司
	 * @param companyAccount
	 * @author sgmark@aliyun.com
	 * @return
	 */
	@DS("slave")
	Company getCurrent(CompanyAccount companyAccount);
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	Company getCompanyByIds(Integer CommunityId, Integer CategoryId);
	@DS("slave")
	IotCompanyResult selectIotUrlByEquipmentCode(String cabinetNo);

	String isOpenOrder(String isOpenOrder, Integer companyId);

	@DS("slave")
	Object companyAreaRanges(String title, String companyId);

	@DS("slave")
	Object getAdminCompanyList(String companyName, String title, PageBean pageBean);

	String deleteCompanyById(Integer companyId);

	@DS("slave")
	Object getCompanyDetail(Integer companyId);

	Object updateCompanyDetail(CompanyBean companyBean);

	@DS("slave")
	Object adminCompanyRangeById(Integer companyId);
	@DS("slave")
	List<Company>  getCompanyList(String companyName);
	@DS("slave")
	List<Company> getCompanyNameList();
	@DS("slave")
	List<Long> getStreetNumByTableName(String tableName);
}
