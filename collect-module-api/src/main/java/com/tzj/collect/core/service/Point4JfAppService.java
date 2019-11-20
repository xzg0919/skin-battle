package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Point;

public interface Point4JfAppService extends IService<Point> {

    Point getPoint(String aliUserId);
}
