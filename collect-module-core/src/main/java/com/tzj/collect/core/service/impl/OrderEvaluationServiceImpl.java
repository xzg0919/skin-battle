package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.utils.MemberUtils;
import com.tzj.collect.core.mapper.OrderEvaluationMapper;
import com.tzj.collect.core.param.ali.OrderEvaluationBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.service.OrderEvaluationService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderEvaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 评价类ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class OrderEvaluationServiceImpl extends ServiceImpl<OrderEvaluationMapper, OrderEvaluation> implements OrderEvaluationService {
    @Autowired
	private OrderService orderService;
	
	@Override
	public Page<OrderEvaluation> selectEvalByRecyclePage(long recId, PageBean page) {
		Page<OrderEvaluation> pages = new Page<OrderEvaluation>(page.getPageNumber(), page.getPageSize());
		EntityWrapper<OrderEvaluation> wrapper = new EntityWrapper<OrderEvaluation>();
		wrapper.eq("recycler_id", recId);
		return this.selectPage(pages, wrapper);
	}

	/*
	     根据回收人员id获取评价数
	 */
	@Override
	public Map<String, Object> getScoreCount(Long recyclerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		//好评数量
		int goodCredit = this.selectCount(new EntityWrapper<OrderEvaluation>().gt("score", 3).eq("recycler_id", recyclerId));
		//中评数量
		int mediumCredit = this.selectCount(new EntityWrapper<OrderEvaluation>().eq("score", 3).eq("recycler_id", recyclerId));
		//差评数量
		int bedReview = this.selectCount(new EntityWrapper<OrderEvaluation>().lt("score", 3).eq("recycler_id", recyclerId));
		map.put("goodCredit", goodCredit);
		map.put("mediumCredit", mediumCredit);
		map.put("bedReview", bedReview);
		
		return map;
	}
	/**
	 * 根据订单号进行评价
	 */
	@Override
	@Transactional(readOnly = false)
	public OrderEvaluation updateOrderEvaluation(OrderEvaluationBean orderEvaluationBean, Order order) {
		
		OrderEvaluation orderEvaluation = new OrderEvaluation();
		   orderEvaluation.setOrderId(orderEvaluationBean.getOrderId());
		   orderEvaluation.setRecyclerId(order.getRecyclerId());
		   orderEvaluation.setScore(orderEvaluationBean.getScore());
		   orderEvaluation.setMemberId(MemberUtils.getMember().getId().intValue());
		   orderEvaluation.setContent(orderEvaluationBean.getContent()==null ? "" :orderEvaluationBean.getContent());
		   orderEvaluation.setDelFlag("0");	
		   this.insert(orderEvaluation);
		   order.setIsEvaluated("1");//修改订单的状态为已评价		   
		   orderService.updateById(order);
		   return orderEvaluation;
	}

}
