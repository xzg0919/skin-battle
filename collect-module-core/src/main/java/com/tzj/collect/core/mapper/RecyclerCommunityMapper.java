
/**
* @Title: SbRecyclerCommunityMapper.java
* @Package com.tzj.collect.mapper
* @Description: 【】
* @date 2018年3月5日 下午1:13:24
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.business.BusinessRecyclerBean;
import com.tzj.collect.core.param.business.RecyclerServiceBean;
import com.tzj.collect.entity.RecyclerCommunity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @ClassName: SbRecyclerCommunityMapper
* @Description: 【】
* @date 2018年3月5日 下午1:13:24
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/

public interface RecyclerCommunityMapper extends BaseMapper<RecyclerCommunity>{

	/**
	 * 根据回收人员的id查看回收服务范围
	 */
	List<RecyclerServiceBean> recyclerServiceList(@Param("recyclerBean") BusinessRecyclerBean recyclerBean);
/*
 * 根据回收人员id获取回收服务范围个数
 */
	int getRecyclerServiceListCount(@Param("recyclerBean") BusinessRecyclerBean recyclerBean);

}
