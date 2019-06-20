package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.entity.PointList;

import java.util.List;

public interface PointListService  extends IService<PointList>{
	/**
	 * 根据会员ID查询用户的积分流水
	 * @category 王灿
	 * @param memberId
	 * @param pageBean
	 * @return PointList
	 */
	@DS("slave")
	List<Object>  getPointListByType(long memberId,PageBean pageBean);
	
}
