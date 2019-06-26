package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.ArrivalTimeLog;

public interface ArrivalTimeLogService extends IService<ArrivalTimeLog>{
	
	
	/**
     * 修改上门回收时间 
     * @author wangcan
     * @param orderId : 订单Id 
     * @param afterDate : 修改后的时间
     * @param afterPeriod : 修改后的 上午am 下午pm
     * @return
     */
	public String sendArrivalTimeLog(Integer orderId,String afterDate,String afterPeriod,String cancleDesc);
}
