
/**
* @Title: SbOrderLogMapper.java
* @Package com.tzj.collect.mapper
* @Description: 【】
* @date 2018年3月5日 下午12:36:16
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.OrderLog;

/**
* @ClassName: SbOrderLogMapper
* @Description: 【】
* @date 2018年3月5日 下午12:36:16
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/

public interface OrderLogMapper extends BaseMapper<OrderLog>{
	/**
	 * 查询某一天的某种状态订单的数量 
	 * @return
	 */
	Integer getOrderData(@Param("id")String id, @Param("startTime")String startTime,@Param("status")String status);
	
	

}
