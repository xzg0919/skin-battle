package com.tzj.collect.api.app;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.app.ArrivalTimeLogBean;
import com.tzj.collect.core.service.ArrivalTimeLogService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.ArrivalTimeLog;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

/**
 * 上门回收时间日志
 * @author Michael_Wang
 *
 */
@ApiService
public class AppArrivalTimeLogApi {
	
	@Autowired
	private ArrivalTimeLogService arrivalTimeLogService;
	@Autowired
	private OrderService orderService;
	
	/**
     * 用户修改上门回收时间 
     * @author wangcan
     * @return
     */
	 @Api(name = "app.sendArrivalTimeLog", version = "1.0")
	 @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	 public String sendArrivalTimeLog(ArrivalTimeLogBean arrivalTimeLogBean){
		//获取订单Id
		 Integer orderId =  arrivalTimeLogBean.getOrderId();
		 int count = arrivalTimeLogService.selectCount(new EntityWrapper<ArrivalTimeLog>().eq("order_id", orderId));
		 if(count>=2) {
			 return "您已修改的次数超过两次，不可再修改";
		 }
		return arrivalTimeLogService.sendArrivalTimeLog(orderId,arrivalTimeLogBean.getAfterDate(),arrivalTimeLogBean.getAfterPeriod(),arrivalTimeLogBean.getCancleDesc());
				 				 
	 }
	 
 	/**
     * 获取修改上门回收时间列表
     * @author wangcan
     * @return
     */
	 @Api(name = "app.getArrivalTimeLogList", version = "1.0")
	 @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	 @DS("slave")
	 public Object getArrivalTimeLogList(ArrivalTimeLogBean arrivalTimeLogBean){
		//获取订单Id
		 Integer orderId =  arrivalTimeLogBean.getOrderId();
		
		return arrivalTimeLogService.selectList(new EntityWrapper<ArrivalTimeLog>().eq("order_id", orderId));
				 				 
	 }
}
