package com.tzj.collect.api.business;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.core.param.admin.LjAdminBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.business.BOrderBean;
import com.tzj.collect.core.result.business.CancelResult;
import com.tzj.collect.core.result.business.OrderServiceabilityResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
	private RecyclersService recyclersService;
	@Autowired
	private RecyclerCancelLogService logService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private  AreaService areaService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private SendRocketmqMessageService sendRocketmqMessageService;
	/**
	 * 根据各种查询条件获取订单 列表
	 * @author 王灿
	 * @param orderBean
	 * @return 
	 * 
	*/
	 @Api(name = "business.order.getOrderLists", version = "1.0")
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String, Object> getOrderLists(BOrderBean orderBean)
	{	//获取分页数据
		PageBean pageBean = orderBean.getPagebean();
		Map<String, Object> orderMap = orderService.getOrderLists(orderBean,pageBean);
		return  orderMap; 
	}
	/**
	 * 再处理订单列表
	  * @author sgmark@aliyun.com
	  * @date 2019/3/25 0025
	  * @param
	  * @return
	  */
	@Api(name = "business.order.getOrderListsDistribute", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String, Object> getOrderListsDistribute(BOrderBean orderBean)
	{	//获取分页数据
		PageBean pageBean = orderBean.getPagebean();
		Map<String, Object> orderMap = orderService.getOrderListsDistribute(orderBean,pageBean);
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
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String, Object> getOrderCounts(BOrderBean orderBean){
		 	//根据各种状态查询相订单表相关的条数
			Map<String, Object> resultMap = orderService.selectCountByStatus(orderBean.getStatus(),orderBean.getCompanyId(), Order.TitleType.valueOf(orderBean.getCategoryType()));
			return  resultMap;
	 }
	
	/**
	 * 根据订单id获取订单详情
	 * @author 王灿
	 * @param
	 * @return
	 */
	 @Api(name = "business.order.getOrderDetail", version = "1.0")
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
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String,Object> getInitDetail(BOrderBean bOrderBean){
		 int orderId = bOrderBean.getId();
		//查询订单详情
		 Map<String,Object> resultMap = orderService.getInitDetail(orderId);
		return resultMap;
	}
	 /**
		 * 根据企业Id和分类Id 获取回收人员列表
		 * @author 王灿
		 * @param
		 * @return
		 */
		 @Api(name = "business.order.getRecyclersList", version = "1.0")
		 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
		 public List<Recyclers> getRecyclersList2(BOrderBean bOrderBean){
			 List<Recyclers> list =  recyclersService.getRecyclersList2(bOrderBean.getCompanyId(),bOrderBean.getId());
			 return list;
		 }
		 
	 /**
	 * 返回取消原因
	 * @author sgmark@aliyun.com
	 * @param
	 * @return
	 */
	 @Api(name = "business.order.getreccanrea", version = "1.0")
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
	 * 派单、驳回、接单 接口
	 * @author 王灿
	 * @param
	 * @return
	 */
	@Api(name = "business.order.callback", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public String callbackForground(OrderBean orderbean) {
		return orderService.callbackForGround(orderbean);
	}
	/**
	 * 企业取消订单
	 * @author 王灿
	 * @param
	 * @return
	 */
	@Api(name = "business.order.cancleOdrer", version = "1.0")
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
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object tosendfiveKgOrder(OrderBean orderbean){
		return  orderService.tosendfiveKgOrder(orderbean.getId());
	}
	/**
	 * test
	  * @author sgmark@aliyun.com
	  * @date 2019/3/27 0027
	  * @param
	  * @return
	  */
	@Api(name = "order.test", version = "1.0")
	@AuthIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	@DS("slave")
	public List<Category> categoriesList(){
		EntityWrapper<Category> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("title", 4);
		entityWrapper.eq("del_flag", 0);
		return categoryService.selectList(entityWrapper);
	}

	/**
	 * test
	 * 根据订单id将订单回调到待派发状态
	 * @date 2019/3/27 0027
	 * @param
	 * @return
	 */
	@Api(name = "business.order.updateStatus", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object orderUpdateStatus(BOrderBean bOrderBean){
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return  orderService.orderUpdateStatus(companyAccount.getCompanyId().toString(),bOrderBean.getOrderId());
	}

	/**
	 * 获取订单的服务能力数据
	 * @author wangmeixia
	 * @date 2019/08/30
	 * @return
	 */
	@Api(name = "business.order.orderServiceability", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<OrderServiceabilityResult> orderServiceability(LjAdminBean ljAdminBean){
		List<OrderServiceabilityResult> data = new ArrayList<OrderServiceabilityResult>();
		//因为原来的service id用的是-1 所以先初始化成默认值 兼容原来的代码
		if(null == ljAdminBean.getAreaId()){
			ljAdminBean.setAreaId("-1");
		}
		if(null == ljAdminBean.getStreetId()){
			ljAdminBean.setStreetId("-1");
		}
		if(null == ljAdminBean.getCityId()){
			ljAdminBean.setCityId("-1");
		}
		//平均派单时间
		Double avgTosendDate = orderService.avgOrMaxDateByOrder(ljAdminBean,"1","avg","2");
		//最长派单时间
		Double maxTosendDate = orderService.avgOrMaxDateByOrder(ljAdminBean,"1","max","2");
		//平均接单时间
		Double avgAlreadyDate = orderService.avgOrMaxDateByOrder(ljAdminBean,"2","avg","2");
		//最长接单时间
		Double maxAlreadyDate = orderService.avgOrMaxDateByOrder(ljAdminBean,"2","max","2");
		//平均完成订单时间
		Double avgCompleteDate = orderService.avgOrMaxDateByOrder(ljAdminBean,"3","avg","2");
		//最长完成订单时间
		Double maxCompleteDate = orderService.avgOrMaxDateByOrder(ljAdminBean,"3","max","2");

		OrderServiceabilityResult result = new OrderServiceabilityResult();
		result.setAvgTosendDate(avgTosendDate);
		result.setMaxTosendDate(maxTosendDate);
		result.setAvgAlreadyDate(avgAlreadyDate);
		result.setMaxAlreadyDate(maxAlreadyDate);
		result.setAvgCompleteDate(avgCompleteDate);
		result.setMaxCompleteDate(maxCompleteDate);

		data.add(result);


		return data;
	}
	/**
	 * 获取回收人员的服务能力数据
	 * @date 2019/08/30
	 * @return
	 */
	@Api(name = "business.order.getReyclersServiceAbility", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getReyclersServiceAbility(OrderBean orderBean){
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return orderService.getReyclersServiceAbility(orderBean,companyAccount.getCompanyId());
	}
	/**
	 * 根据回收人Id查询回收人员正常或者超时的单子
	 * @date 2019/08/30
	 * @return
	 */
	@Api(name = "business.order.overTimeOrderListByReyclersId", version = "1.0")
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object overTimeOrderListByReyclersId(OrderBean orderBean){
		return orderService.overTimeOrderListByReyclersId(orderBean);
	}

}
