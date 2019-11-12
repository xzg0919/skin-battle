package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.OrderEvaluationBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderEvaluation;

import java.util.Map;

/**
 * @Author 王灿
 **/
public interface OrderEvaluationService extends IService<OrderEvaluation> {
	/**
	 * @author sgmark@aliyun.com
	 * @return 评价Page
	 */
	@DS("slave")
	Page<OrderEvaluation> selectEvalByRecyclePage(long recId, PageBean page);

	/**
	 * 根据回收人员id获取评价数
	* @Title: getScoreCount
	* @date 2018年3月27日 下午3:32:38
	* @author:[王池]
	* @param @param recyclerId
	* @param @return    参数
	* @return Map<String,Object>    返回类型
	 */
	@DS("slave")
	Map<String, Object> getScoreCount(Long recyclerId);

	OrderEvaluation updateOrderEvaluation(OrderEvaluationBean orderEvaluationBean, Order order);

	
}
