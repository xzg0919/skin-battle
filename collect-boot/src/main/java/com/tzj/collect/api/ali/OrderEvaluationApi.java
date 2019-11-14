package com.tzj.collect.api.ali;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.OrderEvaluationBean;
import com.tzj.collect.core.service.OrderEvaluationService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderEvaluation;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 评价相关api
 * @Author 王美霞20180305
 **/
@ApiService
public class OrderEvaluationApi {

	@Autowired
	private OrderEvaluationService orderEvaluationService;
	@Autowired
	private OrderService orderService;
    /**
     * 根据订单号取得相关评价
     * @param 
     * @return
     */
	 @Api(name = "evaluation.getEvaluationByOrder", version = "1.0")
	 @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	 public OrderEvaluation getEvaluationByOrder(OrderBean order){
		 return orderEvaluationService.selectOne(new EntityWrapper<OrderEvaluation>().eq("order_id", order.getId()));
		
	 }
	
	
	 /**
     * 根据订单号进行评价
     * @param 
     * @return
     */
	 @Api(name = "evaluation.evaluationByOrder", version = "1.0")
	 @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	 public OrderEvaluation evaluationByOrder(OrderEvaluationBean orderEvaluationBean){
		 
		 Order order = orderService.selectById(orderEvaluationBean.getOrderId()); 		 
		 
	   if("0".equals(order.getIsEvaluated())){
		   return orderEvaluationService.updateOrderEvaluation(orderEvaluationBean,order);
		   
	   }   
	  	 return null;
	 }
	 
}
