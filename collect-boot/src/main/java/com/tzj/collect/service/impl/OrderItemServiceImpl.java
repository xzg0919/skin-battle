package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.ali.param.OrderItemBean;
import com.tzj.collect.api.ali.result.ComCatePrice;
import com.tzj.collect.entity.OrderItem;
import com.tzj.collect.mapper.OrderItemMapper;
import com.tzj.collect.mapper.OrderMapper;
import com.tzj.collect.service.OrderItemService;
import com.tzj.collect.service.OrderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单分类属性明细ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService{
	
	
	@Autowired
	private OrderMapper orderMapper;
	/**
     * 储存订单分类属性明细
     * @author 王灿
     * @param 
     * @return List<Order>:未完成的订单列表
     */
	@Transactional
	@Override
	public boolean saveByOrderItem(OrderItemBean orderItemBean) {
		OrderItem orderItem = new OrderItem();
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
	public List<OrderItem> selectByOrderId(int orderId) {
		EntityWrapper<OrderItem> wrapper = new EntityWrapper<OrderItem>();
		wrapper.eq("order_id", orderId);
		wrapper.eq("del_flag", "0");
		return this.selectList(wrapper);
	}

	@Override
	public List<ComCatePrice> selectCateName(int orderId) {
		return orderMapper.selectCateName(orderId);
	}
	/**
	 * 获取父类名称
	 * @param orderId
	 * @return
	 */
	public List<ComCatePrice> selectCateAchName(int orderId){
		return orderMapper.selectCateAchName(orderId);
	}
}
