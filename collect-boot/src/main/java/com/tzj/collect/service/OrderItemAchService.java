package com.tzj.collect.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.param.OrderItemBean;
import com.tzj.collect.api.ali.result.ComCatePrice;
import com.tzj.collect.entity.OrderItemAch;

/**
 * @Author 王灿
 **/
public interface OrderItemAchService extends IService<OrderItemAch>{
	/**
     * 储存订单分类属性明细
     * @author 王灿
     * @param 
     * @return List<Order>:未完成的订单列表
     */
	boolean saveByOrderItem(OrderItemBean orderItemBean);
	/**
	 * 根据订单Id查询订单分类属性明细
	 * @author 王灿
	 * @param orderId:订单主键
	 * @return List<OrderItem>
	 */
	List<OrderItemAch> selectByOrderId(int orderId);
	/**
	 * 获取父类名称
	 * @param orderId
	 * @return
	 */
	List<ComCatePrice> selectCateName(int orderId);
}
