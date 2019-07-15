package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Point;

public interface PointService  extends IService<Point> {
	/**
	 * 获取积分值接口   
	 * @author 王灿
	 * @param memberId 会员主键
	 * @return Point
	 */
	@DS("slave")
	Point getPoint(long memberId);
	@DS("slave")
	Object getPointLists(long memberId);
}
