package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.api.app.param.RecyclersBean;
import com.tzj.collect.api.app.result.AppCompany;
import com.tzj.collect.api.business.param.BusinessRecyclerBean;
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
	
	List<AppCompany> getRecyclerCompanyStatus(@Param("recyclerId") String recId);

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

	List<Map<String,Object>> getBigCompanyRange(Integer companyId);
}
