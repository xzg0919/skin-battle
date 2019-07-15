
/**
* @Title: SbRecyclersService.java
* @Package com.tzj.collect.service
* @Description: 【】
* @date 2018年3月5日 下午1:37:54
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.core.param.admin.AdminCommunityBean;
import com.tzj.collect.core.param.admin.RecyclersBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.business.BusinessRecyclerBean;
import com.tzj.collect.core.param.business.CompanyAccountBean;
import com.tzj.collect.core.param.business.RecyclersServiceRangeBean;
import com.tzj.collect.entity.Recyclers;

import java.util.List;
import java.util.Map;

/**
* @ClassName: SbRecyclersService
* @Description: 【】
* @date 2018年3月5日 下午1:37:54
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/

public interface RecyclersService extends IService<Recyclers>{

	@DS("slave")
    Recyclers selectByMobile(String mobile);
    
    /**
	 * @author sgmark@aliyun.com
	 * @return 回收人员Page
	 */
	@DS("slave")
	Page<Recyclers> selectByRecyclersPage(Recyclers recyclers, PageBean page);
	
	
	/**
	 * 
	 * @Title: getRecyclerPage 
	 * @Description:管理员端获取回收人员分页信息
	 * @author: 向忠国
	 * @param @param bean
	 * @param @return    设定文件  
	 * @return List<Recyclers>    返回类型  
	 * @throw
	 */
	@DS("slave")
	List<Recyclers> getRecyclerPage(RecyclersBean bean);
	
	/**
	 * 
	 * @Title: getRecyclerPageSize 
	 * @Description:获取回收人员分页总数量 
	 * @author: 向忠国
	 * @param @param bean
	 * @param @return    设定文件  
	 * @return int    返回类型  
	 * @throw
	 */
	@DS("slave")
	Integer getRecyclerPageSize(RecyclersBean bean);

	/**
	 * 根据回收人员id得到个人信息，评价数目
	 * @author sgmark@aliyun.com
	 * @return
	 */
	@DS("slave")
	RecyclersBean getRecEvaById(String recyclerId);
	/**
	 * 根据多个回收员id返回Page
	 * @param ids
	 * @return
	 */
	@DS("slave")
	Page<Recyclers> selectRecPageByIds(String ids, CompanyAccountBean companyAccountBean);
	/**
	 * 根据回收人员id得到服务小区名称，所属区域
	 * @param recyclerId
	 * @return
	 */
	@DS("slave")
	List<AdminCommunityBean> getRecSerCommById(String recyclerId);
	/**
	 * 根据回收人员id得到服务小区总数
	 * @param recyclerId
	 * @return
	 */
	@DS("slave")
	Integer getCommNumByRecId(String recyclerId);


	/**
	 * 根据企业Id和分类Id 获取回收人员列表
	 * @author 王灿
	 * @param companyId:企业Id
	 * @param categoryId : 分类Id
	 * @return
	 */
	@DS("slave")
	List<Recyclers> getRecyclersList(Integer companyId, Integer categoryId);



	/*
	 * 根据回收人员id获取该公司的回收人员详情
	 */
	@DS("slave")
	Recyclers getRecyclersById(BusinessRecyclerBean recyclerBean);
/**
 * 查收该公司的回收人员的申请列表
* @Title: getRecyclersApply
* @date 2018年3月27日 下午5:59:38
* @author:[王池]
* @param @param recyclerBean
* @param @return    参数
* @return List<Recyclers>    返回类型
 */
@DS("slave")
	List<Recyclers> getRecyclersApply(BusinessRecyclerBean recyclerBean);


/**
 * 根据企业Id和分类Id 获取回收人员列表
 * @author sgmark@aliyun.com
 * @param companyId:企业Id
 * @return
 */
	@DS("slave")
	List<Recyclers> getRecyclersList2(Integer companyId, Integer orderId);

	@DS("slave")
	List<Recyclers> getSendOrderRecyclersList(Integer orderId);

	/**
	 * 获取该企业的所有业务经理信息
	 * @author wangcan
	 * @param companyId : 企业Id
	 * @return
	 */
	@DS("slave")
	List<Map<String,Object>> getRecyclers(Integer companyId, String isBigRecycle);

	/**
	 * 保存业务经理，和下属回收人员的信息
	 * @author wangcan
	 * @param recyclersServiceRangeBean
	 * @param companyId : 企业Id
	 * @return
	 */
	Object saveRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean, Integer companyId);

	/**
	 * 保存或更新回收人员区域信息
	 * @param recyclersServiceRangeBean
	 * @param companyId
	 * @return
	 */
	Object updateOrSaveRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean, Integer companyId);
	/**
	 * 保存业务经理更改区域信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	Object updateRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean, Integer companyId);
	/**
	 * 根据市级Id和回收人员id获取区域信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@DS("slave")
	Object getAreaRecyclersRange(String cityId, String recycleId, Integer companyId);
	/**
	 * 根据市级Id和回收人员id获取街道信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@DS("slave")
	Object getStreeRecyclersRange(String areaId, String recycleId, Integer companyId, String title);
	/**
	 * 获取回收经理人员列表
	 * @author wangcan
	 * @param companyId : 企业Id
	 * @return
	 */
	@DS("slave")
	Object getRangeRecyclersList(Integer companyId, String recycleName, String cityId, Integer pageNum, Integer pageSize, String isBigRecycle, String tel);
	/**
	 * 获取回收经理的详细信息
	 * @author wangcan
	 * @param recyclerId : 经理Id
	 * @return
	 */
	@DS("slave")
	List<Map<String,Object>> getRecycleDetails(Integer recyclerId);

	String getAuthCode(String authCode, Long recyclersId) throws ApiException;
	@DS("slave")
	List<Recyclers> getRecyclersListByParentId(Integer companyId, String recycleId, String isBigRecycle);
	@DS("slave")
	Object getAreaRecyclersRangeList(RecyclersServiceRangeBean recyclersServiceRangeBean, String companyId);
}
