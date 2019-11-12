
/**
* @Title: SbRecyclerCommunityService.java
* @Package com.tzj.collect.service
* @Description: 【】
* @date 2018年3月5日 下午1:12:00
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.business.BusinessRecyclerBean;
import com.tzj.collect.core.param.business.RecyclerServiceBean;
import com.tzj.collect.entity.RecyclerCommunity;

import java.util.List;

/**
* @ClassName: SbRecyclerCommunityService
* @Description: 【】
* @date 2018年3月5日 下午1:12:00
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/

public interface RecyclerCommunityService extends IService<RecyclerCommunity>{
	/**
	 * 根据小区Id查询回收人员总数
	 * @author sgmark@aliyun.com
	 * @param ComId
	 * @return 回收人员总数
	 */
	@DS("slave")
	Integer selectRecCountByCom(String ComId);

	/**
	 * 根据回收人员的id查看回收服务范围
	* @Title: recyclerServiceList
	* @date 2018年3月27日 下午2:23:05
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数
	* @return List<RecyclerServiceBean>    返回类型
	 */
	@DS("slave")
	List<RecyclerServiceBean> recyclerServiceList(BusinessRecyclerBean recyclerBean);
/**
 * 根据回收人员的id查看回收服务范围个数
* @Title: getRecyclerServiceListCount
* @date 2018年3月27日 下午4:34:41
* @author:[王池]
* @param @param recyclerBean
* @param @return    参数
* @return int    返回类型
 */	@DS("slave")
	int getRecyclerServiceListCount(BusinessRecyclerBean recyclerBean);
	

	
}
