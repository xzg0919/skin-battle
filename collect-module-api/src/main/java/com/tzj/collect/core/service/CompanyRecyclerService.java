package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.app.RecyclersBean;
import com.tzj.collect.core.param.business.RecyclersServiceRangeBean;
import com.tzj.collect.core.result.app.AppCompany;
import com.tzj.collect.core.param.business.BusinessRecyclerBean;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyRecycler;
import com.tzj.collect.entity.Recyclers;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyRecyclerService extends IService<CompanyRecycler>{
	@DS("slave")
	List<Company> selectCompanyByRecyclerId(@Param("recyclerId") String recyclerId);

	
	/**根据公司ID查询绑定的回收人员个数
	* @Title: selectRecycleByCompanyId
	* @Description: 【】
	* @date 2018年3月20日 上午11:43:36
	* @author:[王池][wjc2013481273@163.com]
	* @param @param id
	* @param @return    参数
	* @return int    返回类型
	* @throws
	*/
	
	int selectRecycleByCompanyId(long id);
	@DS("slave")
	List<CompanyRecycler> selectRecByComId(String id);

	/**
	 * 根据回收人员id返回公司认证状态及公司名称
	 * @param recId
	 * @return 
	 */
	@DS("slave")
	List<AppCompany> getRecyclerCompanyStatus(String recId,String isBigRecycle);

	/**
	 * 根据公司id和回收人员ida查找关联的回收人员
	* @Title: getCompanyRecycler
	* @date 2018年3月28日 上午10:07:47
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数
	* @return CompanyRecycler    返回类型
	 */
	@DS("slave")
	CompanyRecycler getCompanyRecycler(BusinessRecyclerBean recyclerBean);

/**
 * 查看该公司下的回收人员的身份证信息
* @Title: findIdCard
* @date 2018年3月28日 上午10:30:35
* @author:[王池]
* @param @param recyclerBean
* @param @return    参数
* @return Recyclers    返回类型
 */
	@DS("slave")
	Recyclers findIdCard(BusinessRecyclerBean recyclerBean);
	
	/**
	 * 我的公司，得到当前公司列表，已入驻或者申请中
	 * @author sgmark@aliyun.com
	 * @param recyclersBean (recyclerId)
	 * @return
	 */
	@DS("slave")
	 Map<String,Object> getCurrComList(RecyclersBean recyclersBean);

	@DS("slave")
	Map<String,Object> getBigCurrcomList(RecyclersBean recyclersBean);

	/**
	 * 添加公司(未申请或者被拒绝公司列表)
	 * @param recyclersBean
	 * @return
	 */
	@DS("slave")
	Map<String,Object> getNotEnterComList(RecyclersBean recyclersBean);


	boolean insertComRecByComIds(AppCompany appCompanys, long recId);

	Object deleteCompanyRecycle(AppCompany appCompanys, long recId);

     /**
      * 修改回收人员的启用状态
     * @Title: getCompanyRecyclerByRecyclerId
     * @date 2018年4月26日 下午1:02:16
     * @author:[王池]
     * @param @param recyclerId
     * @param @return    参数
     * @return CompanyRecycler    返回类型
      */
     @DS("slave")
	 CompanyRecycler getCompanyRecyclerByRecyclerId(Long recyclerId,String isBigRecycle);

/**
 * 通过条件回收员姓名,id返回某公司回收人员列表
* @Title: getgetSearchCompanyRecyclerList
* @date 2018年4月26日 下午1:54:21
* @author:[王池]
* @param @param recyclerBean
* @param @return    参数
* @return List<CompanyRecycler>    返回类型
 */
	@DS("slave")
	List<BusinessRecyclerBean> getgetSearchCompanyRecyclerList(BusinessRecyclerBean recyclerBean);

	/**
	 * 获取企业服务范围（例南京市，苏州市等）
	 * @author wangcan
	 * @param
	 * @return
	 */
	@DS("slave")
	List<Map<String,Object>> getCompanyRange(Integer companyId);
	/**
	 * 获取企业大件服务范围（例南京市，苏州市等）
	 */
	@DS("slave")
	List<Map<String,Object>> getBigCompanyRange(Integer companyId);
	/**
	 * 获取企业家电服务范围（例南京市，苏州市等）
	 */
	@DS("slave")
	List<Map<String,Object>> getAppliceCompanyRange(Integer companyId);
	/**
	 * 获取企业生活垃圾服务范围（例南京市，苏州市等）
	 */
	@DS("slave")
	List<Map<String,Object>> getHouseCompanyRange(Integer companyId);

	Object recyclersDel(Integer companyId, String recycleId);

	Object recycleDelete(Integer companyId, String recycleId, String title);

	Object recycleIsDelete(Integer companyId, String recycleId, String title);
	@DS("slave")
	Object getRecycleRangeByTitle(String companyId, String recyclerId, String title);

	Object updateRecycleForParent(RecyclersServiceRangeBean recyclersServiceRangeBean, Integer companyId);
	@DS("slave")
    String companyBlueTooth(String recId);
	@DS("slave")
    Map<String, Object> selectRecByHardwareCode(String topic);
	@DS("slave")
	Map<String, Object> getRecyclerList(String recyclerName, String recyclerTel, String cityId, PageBean pageBean);

	String closeRecyclerArea(Long recyclerId,String companyId,String type);
}
