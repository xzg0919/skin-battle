package com.tzj.collect.core.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.PointListMapper;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.service.PointListService;
import com.tzj.collect.core.service.PointService;
import com.tzj.collect.entity.Point;
import com.tzj.collect.entity.PointList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class PointListServiceImpl extends ServiceImpl<PointListMapper, PointList> implements PointListService {
	
	@Autowired
	private PointService pointService;
	/**
	 * 根据会员ID查询用户的积分流水
	 * @category 王灿
	 * @param memberId
	 * @param pageBean
	 * @return PointList
	 */
	@Override
	@DS("slave")
	public List<Object> getPointListByType(long memberId, PageBean pageBean) {
		//获取用户积分表
        Point points = pointService.getPoint(memberId);
        double point = 0;
        double remainPoint = 0;
        if(points !=null) {
        	point = points.getPoint();
        	remainPoint = points.getRemainPoint();
        }
        List<PointList> pointLists = this.selectList(new EntityWrapper<PointList>().eq("member_id", memberId).eq("del_flag", "0").orderBy("create_date", false));
        List<Object> resultList = new ArrayList<Object>(); 
    	resultList.add(point);
    	resultList.add(pointLists);
    	resultList.add(remainPoint);
		return resultList;
	}

}
