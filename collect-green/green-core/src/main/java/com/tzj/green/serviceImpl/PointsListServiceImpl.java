package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;
import com.tzj.green.entity.PointsList;
import com.tzj.green.mapper.PointsListMapper;
import com.tzj.green.service.PointsListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [积分流水表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PointsListServiceImpl extends ServiceImpl<PointsListMapper, PointsList> implements PointsListService
{
    @Resource
    private PointsListMapper PointsListMapper;

}