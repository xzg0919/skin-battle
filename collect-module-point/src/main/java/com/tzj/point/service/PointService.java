package com.tzj.point.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Point;

public interface PointService  extends IService<Point> {

    Point getPoint(String aliUserId);
}
