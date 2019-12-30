package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.app.RecyclersBean;
import com.tzj.collect.core.param.business.BusinessRecyclerBean;
import com.tzj.collect.core.result.app.AppCompany;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyRecycler;
import com.tzj.collect.entity.Recyclers;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author xiaoqiang
 *
 */
public interface CompanyRecyclerMapper extends BaseMapper<CompanyRecycler>{
	List<Company> selectCompanyByRecyclerId(@Param("recyclerId") String recyclerId);

	
	/**根据公司id查询绑定回收人员个数
	* @Title: selectRecycleByCompanyId
	* @Description: 【】
	* @date 2018年3月20日 上午11:45:47
	* @author:[王池][wjc2013481273@163.com]
	* @param @param id
	* @param @return    参数
	* @return int    返回类型
	* @throws
	*/
	
	int selectRecycleByCompanyId(@Param("companyId") long id);
	
	List<AppCompany> getRecyclerCompanyStatus(@Param("recyclerId") String recId,@Param("isBigRecycle") String isBigRecycle);

	/**
	 * 查看该公司下的回收人员的身份证信息
	* @Title: findIdCard
	* @date 2018年3月28日 上午10:32:07
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数
	* @return Recyclers    返回类型
	 */
	Recyclers findIdCard(@Param("recyclerBean") BusinessRecyclerBean recyclerBean);


	List<AppCompany> getCurrComList(@Param("recyclerBean") RecyclersBean recyclersBean, @Param("startSize") int startSize, @Param("pageSize") int pageSize);

	List<AppCompany> getBigCurrcomList(@Param("recyclerBean") RecyclersBean recyclersBean, @Param("startSize") int startSize, @Param("pageSize") int pageSize);
	
	Integer getCurrComCount(@Param("recyclerBean") RecyclersBean recyclersBean);

	List<AppCompany> getNotEnterComList(@Param("recyclerBean") RecyclersBean recyclersBean, @Param("startSize") int startSize, @Param("pageSize") int pageSize);
	
	Integer getNotEnterComCount(@Param("recyclerBean") RecyclersBean recyclersBean);
	/**
	 * 得到公司相关业务范围
	 * @param recyclersBean
	 * @return
	 */
	List<AppCompany> getComCateList(@Param("recyclerBean") RecyclersBean recyclersBean);

/**
 * 通过搜索条件回收人员姓名和id来查询公司人员列表
* @Title: getgetSearchCompanyRecyclerList
* @date 2018年4月26日 下午1:56:24
* @author:[王池]
* @param @param recyclerBean
* @param @return    参数
* @return List<CompanyRecycler>    返回类型
 */
	List<BusinessRecyclerBean> getgetSearchCompanyRecyclerList(@Param("recyclerBean") BusinessRecyclerBean recyclerBean);
	/**
	 * 获取企业服务范围（例南京市，苏州市等）
	 * @author wangcan
	 * @param
	 * @return
	 */
	List<Map<String,Object>> getCompanyRange(Integer companyId);
	/**
	 * 获取企业大件服务范围（例南京市，苏州市等）
	 */
	List<Map<String,Object>> getBigCompanyRange(Integer companyId);
	/**
	 * 获取企业家电服务范围（例南京市，苏州市等）
	 */
	List<Map<String,Object>> getAppliceCompanyRange(Integer companyId);
	/**
	 * 获取企业生活垃圾服务范围（例南京市，苏州市等）
	 */
	List<Map<String,Object>> getHouseCompanyRange(Integer companyId);

    Map<String, Object> companyBlueTooth(@Param("recId")String recId);
	/**
	 * 根据公司和回收类型查找回收人员信息（主要是电话）
	 * @author: sgmark@aliyun.com
	 * @Date: 2019/10/29 0029
	 * @Param: 
	 * @return: 
	 */
	List<Map<String, Object>> selectRecycleInfoByCompanyId(@Param("recId")Long companyId, @Param("title")String title);

    Map<String, Object> selectRecByHardwareCode(@Param("topic")String topic);

	List<Map<String, Object>> getRecyclerList(@Param("recyclerName")String recyclerName,@Param("recyclerTel")String recyclerTel,@Param("cityId")String cityId,@Param("pageStart")Integer pageStart,@Param("pageSize")Integer pageSize);

	Integer getRecyclerCount(@Param("recyclerName")String recyclerName,@Param("recyclerTel")String recyclerTel,@Param("cityId")String cityId);

	List<Map<String, Object>> getRecyclerSonList(@Param("recyclerId")String recyclerId,@Param("type")String type);

	void closeCompanyApplianceByRecyclerId(@Param("companyId")String companyId,@Param("recyclerId")Long recyclerId);

	void closeCompanyHouseByRecyclerId(@Param("companyId")String companyId,@Param("recyclerId")Long recyclerId);

	void closeCompanyBigByRecyclerId(@Param("companyId")String companyId,@Param("recyclerId")Long recyclerId);

	void updateRecycler(@Param("recyclerId")Long recyclerId,@Param("type")String type);
}
