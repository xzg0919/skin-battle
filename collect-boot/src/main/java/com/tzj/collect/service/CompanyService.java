package com.tzj.collect.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.result.BusinessRecType;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyAccount;

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
	
	Page<Company> getSearchCompany(CompanyBean companybean, PageBean pageBean);

	

	/**
	 * @author sgmark@aliyun.com
	 * 根据小区Id取得回收企业总数
	 * @param comId
	 * @return
	 */
	Integer selectCompanyCountByCom(String comId);
	/**
	 * @author sgmark@aliyun.com
	 * 根据小区Id取得回收企业回收垃圾类型（去重）
	 * @param comId
	 * @return
	 */
	Integer selectCategoryCountByCom(String comId);
	/**
	 * 根据小区id取得服务公司列表
	 * @author sgmark@aliyun.com
	 * @param comId
	 * @return
	 */
	List<Company> selectCompanyListByComm(String commId);

	/**
	 * 根据公司id返回公司回收类型
	 * @param comId
	 * @return
	 */
	List<BusinessRecType> getTypeByComId(String comId); 
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业Id
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	Integer getCompanyIdByIds(Integer CommunityId,Integer CategoryId);

	/**
	 * 根据公司账号获取当前公司
	 * @param companyAccount
	 * @author sgmark@aliyun.com
	 * @return
	 */
	Company getCurrent(CompanyAccount companyAccount);
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	Company getCompanyByIds(Integer CommunityId,Integer CategoryId);
}
