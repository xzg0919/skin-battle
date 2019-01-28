package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.business.result.CancelResult;
import com.tzj.collect.entity.RecyclerCancelLog;


/**
 * @Author zhangqiang
 **/
public interface RecyclerCancelLogService extends IService<RecyclerCancelLog>{
	//RecyclerCancelLog selectCancelReason(OrderBean orderbean);
	CancelResult selectCancel(OrderBean orderbean);
}
