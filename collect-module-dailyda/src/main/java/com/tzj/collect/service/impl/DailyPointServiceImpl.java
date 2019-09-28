package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Point;
import com.tzj.collect.entity.PointList;
import com.tzj.collect.mapper.DailyPointMapper;
import com.tzj.collect.service.DailyPointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(readOnly=true)
public class DailyPointServiceImpl extends ServiceImpl<DailyPointMapper, Point> implements DailyPointService {

	@Resource
	private  DailyPointMapper pointMapper;
	/**
	 * 获取积分值接口   
	 * @author 王灿
	 * @return Point
	 */
	@Override
	public Point getPoint(String aliUserId) {
		EntityWrapper entityWrapper = new EntityWrapper<Point>();
				entityWrapper.eq("ali_user_id", aliUserId);
				entityWrapper.eq("del_flag", "0");
		return this.selectOne(entityWrapper);
	}

	@Override
	@Transactional(readOnly = false)
	public void insertPointList(PointList pointList) {
		pointMapper.insertPointList(pointList);
	}

}
