package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.business.result.CancelResult;
import com.tzj.collect.entity.RecyclerCancelLog;
import com.tzj.collect.mapper.RecyclerCancelLogMapper;
import com.tzj.collect.service.RecyclerCancelLogService;


@Service
@Transactional(readOnly = true)
public class RecyclerCancelLogServiceImpl extends ServiceImpl<RecyclerCancelLogMapper ,RecyclerCancelLog> implements RecyclerCancelLogService {
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
