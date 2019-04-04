package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Point;
import com.tzj.collect.entity.PointList;
import com.tzj.collect.mapper.PointMapper;
import com.tzj.collect.service.PointListService;
import com.tzj.collect.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true)
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements PointService {
	@Autowired
	private PointListService pointListService;
	
	/**
	 * 获取积分值接口   
	 * @author 王灿
	 * @return Point
	 */
	@Override
	public Point getPoint(long memberId) {
		EntityWrapper entityWrapper = new EntityWrapper<Point>();
				entityWrapper.eq("member_id", memberId);
				entityWrapper.eq("del_flag", "0");
		return this.selectOne(entityWrapper);
	}

	@Override
	public Object getPointLists(long memberId) {
		Map<String,Object> resultMap = new HashMap<>();
		//获取用户能量信息
		Point point = this.selectOne(new EntityWrapper<Point>().eq("member_id", memberId).eq("del_flag", "0"));
		//获取用户能量流水信息
		List<PointList> inPointList = pointListService.selectList(new EntityWrapper<PointList>().eq("member_id", memberId).eq("del_flag", 0).ge("point", 1).eq("type", 0).orderBy("create_date",false));
		List<PointList> outPointList = pointListService.selectList(new EntityWrapper<PointList>().eq("member_id", memberId).eq("del_flag", 0).le("point", -1).eq("type", 1).orderBy("create_date",false));
		resultMap.put("point",point.getPoint());
		resultMap.put("inPointList",inPointList);
		resultMap.put("outPointList",outPointList);
		return resultMap;
	}

}
