package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.OrderItemMapper;
import com.tzj.collect.core.mapper.OrderMapper;
import com.tzj.collect.core.param.ali.OrderItemBean;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.service.OrderItemService;
import com.tzj.collect.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 订单分类属性明细ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
	@Autowired
	private OrderItemMapper orderItemMapper;
	
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

	@Override
	public Map<String, Object> selectItemOne(Integer orderId) {
		return orderItemMapper.selectItemOne(orderId);
	}

	@Override
	public List<Map<String, Object>> getOrderItemDetail(Long orderId, String isCash) {
		return orderItemMapper.getOrderItemDetail(orderId,isCash);
	}
}
