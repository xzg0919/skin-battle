package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.OrderItemBean;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.entity.OrderItem;

import java.util.List;
import java.util.Map;

/**
 * @Author 王灿
 **/
public interface OrderItemService extends IService<OrderItem>{
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
	@DS("slave")
	List<OrderItem> selectByOrderId(int orderId);
	/**
	 * 获取父类名称
	 * @param orderId
	 * @return
	 */
	@DS("slave")
	List<ComCatePrice> selectCateName(int orderId);
	/**
	 * 获取父类名称
	 * @param orderId
	 * @return
	 */
	@DS("slave")
	List<ComCatePrice> selectCateAchName(int orderId);
	@DS("slave")
	Map<String, Object> selectItemOne(Integer orderId);
	@DS("slave")
	List<Map<String,Object>> getOrderItemDetail(Long orderId,String isCash);
	@DS("slave")
	List<OrderItem> selectByOrderIdName(Integer orderId);
}
