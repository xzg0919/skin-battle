package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CompanyServiceRange;


/**
 * @Author 王灿
 **/
public interface CompanyServiceService extends IService<CompanyServiceRange>{

	
	/**根据企业id获取
	* @Title: selectCompanyServiceByCompanyId
	* @Description: 【】
	* @date 2018年3月20日 下午12:11:03
	* @author:[王池][wjc2013481273@163.com]
	* @param @param id
	* @param @return    参数
	* @return int    返回类型
	* @throws
	*/
	
	int selectCompanyServiceByCompanyId(long id);
/**
 * 根据小区id查找要取消的服务范围小区
* @Title: selectRangeByCommunityId
* @date 2018年3月30日 下午1:51:40
* @author:[王池]
* @param @param delectCommunityId
* @param @return    参数
* @return CompanyServiceRange    返回类型
 */
	CompanyServiceRange selectRangeByCommunityId(Integer delectCommunityId);

}
