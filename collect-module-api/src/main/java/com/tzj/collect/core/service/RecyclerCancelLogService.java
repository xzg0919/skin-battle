package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.result.business.CancelResult;
import com.tzj.collect.entity.RecyclerCancelLog;


/**
 * @Author sgmark@aliyun.com
 **/
public interface RecyclerCancelLogService extends IService<RecyclerCancelLog>{
	//RecyclerCancelLog selectCancelReason(OrderBean orderbean);
	@DS("slave")
	CancelResult selectCancel(OrderBean orderbean);
}
