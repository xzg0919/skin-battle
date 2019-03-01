package com.tzj.collect.api.business;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.param.BOrderBean;
import com.tzj.collect.api.business.result.CancelResult;
import com.tzj.collect.common.constant.RocketMqConst;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderLog;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.service.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;
import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;


/**
 * 20180316
 * @author Michael_Wang
 *
 */
@ApiService
public class BusinessOrderApi {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private CompanyService  companyService;
	@Autowired
	private OrderEvaluationService orderEvaluationService;
	@Autowired
	private RecyclersService recyclersService;
	@Autowired
	private OrderPicService orderPicService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private RecyclerCancelLogService logService;
	/**
	 * 根据各种查询条件获取订单 列表
	 * @author 王灿
	 * @param orderBean
	 * @return 
	 * 
	*/
	 @Api(name = "business.order.getOrderLists", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String, Object> getOrderLists(BOrderBean orderBean)
	{	//获取分页数据
		PageBean pageBean = orderBean.getPagebean(); 
		Map<String, Object> orderMap = orderService.getOrderLists(orderBean,pageBean);
		return  orderMap; 
	}	

	 /**
	 * 根据各种状态的订单条数
	 * @author 王灿
	 * @param orderBean
	 * @return 
	 * 
	*/
	 @Api(name = "business.order.getOrderCounts", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String, Object> getOrderCounts(BOrderBean orderBean){
		 	//根据各种状态查询相订单表相关的条数
			Map<String, Object> resultMap = orderService.selectCountByStatus(orderBean.getStatus(),orderBean.getCompanyId(), orderBean.getCategoryType());
			return  resultMap; 
	 }
	
	/**
	 * 根据订单id获取订单详情
	 * @author 王灿
	 * @param
	 * @return
	 */
	 @Api(name = "business.order.getOrderDetail", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String,Object> getOrderDetail(BOrderBean bOrderBean){
		 int orderId = bOrderBean.getId();
		//查询订单详情
		 Map<String,Object> resultMap = orderService.selectOrderByBusiness(orderId);
		return resultMap;
	}
 /**
	 * 获取用户提交生活垃圾订单id详情
	 * @author 王灿
	 * @param
	 * @return
	 */
	 @Api(name = "business.order.getInitDetail", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String,Object> getInitDetail(BOrderBean bOrderBean){
		 int orderId = bOrderBean.getId();
		//查询订单详情
		 Map<String,Object> resultMap = orderService.getInitDetail(orderId);
		return resultMap;
	}
	 
//	 /**
//	 * 根据企业Id和分类Id 获取回收人员列表
//	 * @author 王灿
//	 * @param
//	 * @return
//	 */
//	 @Api(name = "business.order.getRecyclersList", version = "1.0")
//	 @SignIgnore
//	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
//	 public List<Recyclers> getRecyclersList(BOrderBean bOrderBean){
//		 List<Recyclers> list =  recyclersService.getRecyclersList(bOrderBean.getCompanyId(),bOrderBean.getCategoryId());
//		 return list;
//	 }
	 
	 /**
		 * 根据企业Id和分类Id 获取回收人员列表
		 * @author 王灿
		 * @param
		 * @return
		 */
		 @Api(name = "business.order.getRecyclersList", version = "1.0")
		 @SignIgnore
		 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
		 public List<Recyclers> getRecyclersList2(BOrderBean bOrderBean){
			 List<Recyclers> list =  recyclersService.getRecyclersList2(bOrderBean.getCompanyId(),bOrderBean.getId());
			 return list;
		 }
		 
	 /**
	 * 返回取消原因
	 * @author zhangqiang
	 * @param
	 * @return
	 */
	 @Api(name = "business.order.getreccanrea", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	 public CancelResult getRecCanReaLog(OrderBean OrderBean){
		 return logService.selectCancel(OrderBean);
	 }
	 
	 /**
	 * 派单、驳回、接单 接口
	 * @author 王灿
	 * @param
	 * @return
	 */
	 @Api(name = "business.order.updateOdrerStatus", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public String updateOdrerStatus(BOrderBean orderbean) {
		 //订单id 
		 Integer orderId = orderbean.getId();
		 //要改成的订单状态REJECTED驳回,ALREADY接单,TOSEND派单
		 String status = orderbean.getStatus();
		 //驳回原因
		 String cancelReason = orderbean.getCancelReason();
		 //派单回收员的Id
		 Integer recyclerId = orderbean.getRecyclerId();
		 String sta = orderService.updateOrderByBusiness(orderId,status,cancelReason,recyclerId);
	     return sta;
	}
	/**
	 * 企业取消订单
	 * @author 王灿
	 * @param
	 * @return
	 */
	@Api(name = "business.order.cancleOdrer", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public String cancleOdrer(BOrderBean orderbean) {
		Order order = orderService.selectById(orderbean.getId());
		//修改订单日志表的
		OrderLog orderLog = new OrderLog();
		orderLog.setOpStatusBefore(order.getStatus().toString());
		orderLog.setOrderId(order.getId().intValue());
		order.setStatus(Order.OrderType.CANCEL);
		//驳回原因
		order.setCancelReason(orderbean.getCancelReason());
		//驳回时间
		order.setCancelTime(new Date());
		orderLog.setOpStatusAfter("CANCEL");
		orderLog.setOp("已取消");
		orderService.updateById(order);
		//修改订单日志表的
		orderLogService.insert(orderLog);
		return "SUCCESS";
	}
	/**
	 * 派发5公斤废纺衣物的订单
	 * @return
	 */
	@Api(name = "order.tosendfiveKgOrder", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object tosendfiveKgOrder(OrderBean orderbean){
		Order order = orderService.selectById(orderbean.getId());
		order.setDistributeTime(new Date());
		order.setStatus(Order.OrderType.TOSEND);
		orderService.updateById(order);
		try{
			HashMap<String,Object> param=new HashMap<>();
			param.put("orderNo",order.getOrderNo());
			param.put("orderType","废纺衣物");
			param.put("orderAmount",order.getQty());
			param.put("userName",order.getLinkMan());
			param.put("userTel", order.getTel());
			param.put("userAddress",order.getAddress()+order.getFullAddress());
			param.put("arrivalTime",order.getArrivalTime()+" "+order.getArrivalPeriod());
			param.put("isCancel","N");
			RocketMqConst.sendDeliveryOrder(JSON.toJSONString(param),RocketMqConst.TOPIC_NAME_DELIVERY_ORDER);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "操作成功";
	}
}
