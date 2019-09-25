package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Point;
import com.tzj.collect.entity.PointList;
import org.springframework.scheduling.annotation.Async;

public interface DailyPointService extends IService<Point> {
	/**
	 * 获取积分值接口   
	 * @author 王灿
	 * @return Point
	 */
	@DS("slave")
	Point getPoint(String aliUserId);
	@Async
	void insertPointList(PointList pointList);
}
