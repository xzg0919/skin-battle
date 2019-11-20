package com.tzj.point.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.PointList;
import com.tzj.point.mapper.PointListMapper;
import com.tzj.collect.core.service.PointList4JfAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class PointList4JfApp4JfAppServiceImpl extends ServiceImpl<PointListMapper, PointList> implements PointList4JfAppService {
	


}
