package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;
import com.tzj.green.entity.PointsList;
import com.tzj.green.mapper.PointsListMapper;
import com.tzj.green.param.PageBean;
import com.tzj.green.param.PointsListBean;
import com.tzj.green.service.PointsListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private PointsListMapper pointsListMapper;

    @Override
    public Object getPointsListByCompanyId(Long companyId, PointsListBean pointsListBean) {
        PageBean pageBean = pointsListBean.getPageBean();
        if (null == pageBean){
            pageBean = new PageBean();
        }
        Integer pageStart = (pageBean.getPageNum()-1)*pageBean.getPageSize();
        List<Map<String, Object>> pointsList = pointsListMapper.getPointsListByCompanyId(companyId, pointsListBean.getPointsType(), pointsListBean.getStartTime(), pointsListBean.getEndTime(), pointsListBean.getName(), pointsListBean.getTel(), pointsListBean.getCityId(), pointsListBean.getAreaId(), pointsListBean.getStreetId(), pointsListBean.getCommunityId(), pointsListBean.getCommunityHouseId(), pageStart, pageBean.getPageSize());
        Integer count = pointsListMapper.getPointsListCount(companyId, pointsListBean.getPointsType(), pointsListBean.getStartTime(), pointsListBean.getEndTime(), pointsListBean.getName(), pointsListBean.getTel(), pointsListBean.getCityId(), pointsListBean.getAreaId(), pointsListBean.getStreetId(), pointsListBean.getCommunityId(), pointsListBean.getCommunityHouseId());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pointsList",pointsList);
        resultMap.put("count",count);
        resultMap.put("pageNum",pageBean.getPageNum());
        return resultMap;
    }
}