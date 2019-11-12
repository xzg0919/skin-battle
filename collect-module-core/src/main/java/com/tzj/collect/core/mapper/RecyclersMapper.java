
/**
* @Title: SbRecyclersMapper.java
* @Package com.tzj.collect.mapper
* @Description: 【】
* @date 2018年3月5日 下午1:40:32
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.admin.AdminCommunityBean;
import com.tzj.collect.core.param.admin.RecyclersBean;
import com.tzj.collect.core.param.business.BusinessRecyclerBean;
import com.tzj.collect.entity.Recyclers;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @ClassName: SbRecyclersMapper
* @Description: 【】
* @date 2018年3月5日 下午1:40:32
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/

public interface RecyclersMapper extends BaseMapper<Recyclers>{
	
      public List<Recyclers> getRecyclerPage(@Param("recycler") RecyclersBean bean);
      
      public Integer getRecyclerPageSize(@Param("recycler") RecyclersBean bean);

      
      RecyclersBean getRecEvaById(@Param("recyclerId") String recyclerId);
      
      List<AdminCommunityBean> getRecSerCommById(@Param("recyclerId") String recyclerId);

      Integer getCommNumByRecId(@Param("recyclerId") String recyclerId);


      /**
  	 * 根据企业Id和分类Id 获取回收人员列表
  	 * @author 王灿
  	 * @param companyId :  企业Id
  	 * @param categoryId : 分类Id
  	 * @return
  	 */
      public List<Recyclers> getRecyclersList(@Param("companyId") Integer companyId, @Param("categoryId") Integer categoryId);


	/**
	 * 根据回收人员id获取回收人员的详情
	* @Title: getRecyclersById
	* @date 2018年3月27日 下午2:52:15
	* @author:[王池]
	* @param @param recyclerId
	* @param @return    参数
	* @return Recyclers    返回类型
	 */
	public Recyclers getRecyclersById(@Param("recyclerBean") BusinessRecyclerBean recyclerBean);
	/*
	 * 查询回收人员的申请列表
	 */
	public List<Recyclers> getRecyclersApply(@Param("companyId") long companyId, @Param("isBigRecycle") String isBigRecycle);



	public List<Recyclers> getRecyclersLists(@Param("companyId") Integer companyId, @Param("orderId") Integer orderId, @Param("title") Integer title);

	public List<Recyclers> getSendOrderRecyclersList(@Param("companyId") Integer companyId, @Param("orderId") Integer orderId, @Param("title") Integer title);

	/**
	 * 获取该企业的所有业务经理信息
	 * @author wangcan
	 * @param companyId : 企业Id
	 * @return
	 */
    List<Map<String,Object>> getRecyclers(@Param("companyId") Integer companyId, @Param("isBigRecycle") String isBigRecycle);

	/**
	 * 根据市级Id和回收人员id获取区域信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	public List<Map<String,Object>> getAreaRecyclersRange(@Param("cityId") String cityId, @Param("recycleId") String recycleId);
	/**
	 * 根据市级Id和回收人员id获取街道信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	public List<Map<String,Object>> getStreeRecyclersRange(@Param("areaId") String areaId, @Param("recycleId") String recycleId);
	/**
	 * 获取回收经理人员列表
	 * @author wangcan
	 * @param companyId : 企业Id
	 * @return
	 */
	List<Map<String,Object>> getRangeRecyclersList(@Param("companyId") String companyId, @Param("recycleName") String recycleName, @Param("cityId") String cityId, @Param("pageStartCount") Integer pageStartCount, @Param("pageSize") Integer pageSize, @Param("isBigRecycle") String isBigRecycle, @Param("tel") String tel);
	/**
	 * 获取回收经理人员条数
	 * @author wangcan
	 * @param companyId : 企业Id
	 * @return
	 */
	Integer getRangeRecyclersListCount(@Param("companyId") String companyId, @Param("recycleName") String recycleName, @Param("cityId") String cityId, @Param("isBigRecycle") String isBigRecycle, @Param("tel") String tel);
	/**
	 * 获取回收经理的详细信息
	 * @author wangcan
	 * @param recyclerId : 经理Id
	 * @return
	 */
	List<Map<String,Object>> getRecycleDetails(@Param("recyclerId") Integer recyclerId,@Param("isBigRecycle")String isBigRecycle,@Param("companyId")Integer companyId);

	List<Recyclers> getRecyclersListByParentId(@Param("companyId") Integer companyId, @Param("recycleId") String recycleId, @Param("isBigRecycle") String isBigRecycle);

	List<Recyclers> getRecycleSon(@Param("recyclerId") Long recyclerId,@Param("isBigRecycle") String isBigRecycle,@Param("recyclerName") String recyclerName);

	Integer getRecyclersCountByLj(@Param("companyId")String companyId);
}
