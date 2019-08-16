package com.tzj.point.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Point;
import com.tzj.point.mapper.PointMapper;
import com.tzj.point.service.PointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true)
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements PointService {


    @Override
    public Point getPoint(String aliUserId) {
        EntityWrapper entityWrapper = new EntityWrapper<Point>();
        entityWrapper.eq("ali_user_id", aliUserId);
        entityWrapper.eq("del_flag", "0");
        return this.selectOne(entityWrapper);
    }
}
