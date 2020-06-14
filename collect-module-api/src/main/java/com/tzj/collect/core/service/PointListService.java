package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.entity.PointList;

import java.util.List;

public interface PointListService  extends IService<PointList>{
	/**
	 * 根据会员ID查询用户的积分流水
	 * @category 王灿
	 * @param pageBean
	 * @return PointList
	 */
	@DS("slave")
	List<Object>  getPointListByType(String aliUserId, PageBean pageBean);

	/*
	订单、积分、积分流水表中相应添加aliuserId 和 card_no
	 */
	void updatePointAndOrderFromDsdd(String aliUserId, String mobile, String cardNo);
}
