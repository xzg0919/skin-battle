package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Point;

public interface PointService  extends IService<Point> {
	/**
	 * 获取积分值接口   
	 * @author 王灿
	 * @return Point
	 */
	@DS("slave")
	Point getPoint(String aliUserId);
	@DS("slave")
	Object getPointLists(String aliUserId);
}
