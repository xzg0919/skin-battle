package com.tzj.collect.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.business.param.BOrderBean;
import com.tzj.collect.api.business.result.CancelResult;
import com.tzj.collect.entity.RecyclerCancelLog;

public interface RecyclerCancelLogMapper extends BaseMapper<RecyclerCancelLog>{
	CancelResult selectCancel(@Param("orderBean")OrderBean orderbean);
}
