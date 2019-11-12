package com.tzj.collect.api.business;

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

import static com.tzj.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

/**
 * 上门回收时间日志
 * @author Michael_Wang
 *
 */
@ApiService
public class BusinessArrivalTimeLogApi {
	
	@Autowired
	private ArrivalTimeLogService arrivalTimeLogService;
	@Autowired
	private OrderService orderService;
	
	/**
     * 获取修改上门回收时间列表
     * @author wangcan
     * @return
     */
	 @Api(name = "business.getArrivalTimeLogList", version = "1.0")
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	 @DS("slave")
	 public Object getArrivalTimeLogList(ArrivalTimeLogBean arrivalTimeLogBean){
		//获取订单Id
		 Integer orderId =  arrivalTimeLogBean.getOrderId();
		
		return arrivalTimeLogService.selectList(new EntityWrapper<ArrivalTimeLog>().eq("order_id", orderId));
				 				 
	 }
}
