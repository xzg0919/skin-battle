package com.tzj.collect.service.impl;

import java.util.List;
import java.util.Map;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tzj.collect.controller.admin.param.ResultDataVParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.ali.param.OrderItemBean;
import com.tzj.collect.api.ali.result.ComCatePrice;
import com.tzj.collect.entity.OrderItem;
import com.tzj.collect.entity.OrderItemAch;
import com.tzj.collect.mapper.OrderItemAchMapper;
import com.tzj.collect.mapper.OrderMapper;
import com.tzj.collect.service.OrderItemAchService;

/**
 * 订单分类属性明细ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class OrderItemAchServiceImpl extends ServiceImpl<OrderItemAchMapper, OrderItemAch> implements OrderItemAchService{
	
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private  OrderItemAchMapper orderItemAchMapper;
	/**
     * 储存订单分类属性明细
     * @author 王灿
     * @param 
     * @return List<Order>:未完成的订单列表
     */
	@Transactional
	@Override
	public boolean saveByOrderItem(OrderItemBean orderItemBean) {
		OrderItemAch orderItem = new OrderItemAch();
		orderItem.setCategoryId(orderItemBean.getCategoryId());
		orderItem.setCategoryName(orderItemBean.getCategoryName());
		orderItem.setCategoryAttrId(orderItemBean.getCategoryAttrId());
		orderItem.setCategoryAttrName(orderItemBean.getCategoryAttrName());
		orderItem.setCategoryAttrOppId(orderItemBean.getCategoryAttrOppId());
		orderItem.setCategoryAttrOpptionName(orderItemBean.getCategoryAttrOpptionName());
		orderItem.setOrderId(orderItemBean.getOrderId());
		return this.insert(orderItem);
	}
	
	/**
	 * 根据订单Id查询订单分类属性明细
	 * @author 王灿
	 * @param orderId:订单主键
	 * @return List<OrderItem>
	 */
	@Override
	@DS("slave")
	public List<OrderItemAch> selectByOrderId(int orderId) {
		EntityWrapper<OrderItemAch> wrapper = new EntityWrapper<OrderItemAch>();
		wrapper.eq("order_id", orderId);
		wrapper.eq("del_flag", "0");
		return this.selectList(wrapper);
	}

	@Override
	@DS("slave")
	public List<ComCatePrice> selectCateName(int orderId) {
		return orderMapper.selectCateName(orderId);
	}

	@Override
	@DS("slave")
	public List<Map<String, Object>> selectItemSumAmount(Integer orderId) {

		return orderItemAchMapper.selectItemSumAmount(orderId);
	}

	@Override
	@DS("slave")
	public String orderSum(String streetId) {
		return orderItemAchMapper.orderSum(streetId);
	}
	@Override
	@DS("slave")
	public List<ResultDataVParam> orderDetialNum(String streetId) {
		return orderItemAchMapper.orderDetialNum(streetId);
	}


}
