package com.tzj.point.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Point;
import com.tzj.collect.entity.PointList;
import com.tzj.point.mapper.PointListMapper;
import com.tzj.point.service.PointListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class PointListServiceImpl extends ServiceImpl<PointListMapper, PointList> implements PointListService {
	


}
