package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RecyclerCancelLogMapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.result.business.CancelResult;
import com.tzj.collect.core.service.RecyclerCancelLogService;
import com.tzj.collect.entity.RecyclerCancelLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class RecyclerCancelLogServiceImpl extends ServiceImpl<RecyclerCancelLogMapper,RecyclerCancelLog> implements RecyclerCancelLogService {
	@Autowired
	private RecyclerCancelLogMapper recyclerCancelLogMapper;
//	@Override
//	public RecyclerCancelLog selectCancelReason(OrderBean orderbean) {
//		return recyclerCancelLogMapper.selectCacel(orderbean);
//	}
	@Override
	public CancelResult selectCancel(OrderBean orderbean) {
		return recyclerCancelLogMapper.selectCancel(orderbean);
	}

}
