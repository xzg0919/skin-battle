package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.result.business.CancelResult;
import com.tzj.collect.entity.RecyclerCancelLog;
import org.apache.ibatis.annotations.Param;

public interface RecyclerCancelLogMapper extends BaseMapper<RecyclerCancelLog>{
	CancelResult selectCancel(@Param("orderBean") OrderBean orderbean);
}
