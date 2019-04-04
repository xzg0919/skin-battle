package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Point;

public interface PointService  extends IService<Point> {
	/**
	 * 获取积分值接口   
	 * @author 王灿
	 * @param memberId 会员主键
	 * @return Point
	 */
	Point getPoint(long memberId);

	Object getPointLists(long memberId);
}
