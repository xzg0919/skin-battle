package com.skin.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.PointListMapper;
import com.skin.core.service.PointListService;
import com.skin.core.service.SysParamsService;
import com.skin.entity.PointInfo;
import com.skin.entity.PointList;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 16:55
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class PointListServiceImpl extends ServiceImpl<PointListMapper, PointList> implements PointListService {

    @Autowired
    SysParamsService sysParamsService;

    @Override
    public Page<Map<String, Objects>> getPointListPage(Integer pageNum, Integer pageSize, Long userId, Integer orderFrom,String orderNo) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("order_from", orderFrom);
        if(StringUtils.isNotBlank(orderNo)){
            queryWrapper.eq("order_no", orderNo);
        }
        queryWrapper.select("create_date as createDate,order_no as orderNo,amount");
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public BigDecimal sumPoint(Integer orderFrom) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_from", orderFrom);
        PointList pointList = baseMapper.selectOne(queryWrapper.select("sum(point) as point"));
        return pointList == null ? BigDecimal.ZERO : pointList.getPoint();
    }

    @Override
    public BigDecimal sumPoint(Integer orderFrom, String startTime, String endTime) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_from", orderFrom);
        queryWrapper.ge("create_date", startTime);
        queryWrapper.le("create_date", endTime);
        PointList pointList = baseMapper.selectOne(queryWrapper.select("sum(point) as point"));
        return pointList == null ? BigDecimal.ZERO : pointList.getPoint();
    }

    @Override
    public Integer consumeCount(Integer orderFrom) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_from", orderFrom);
        return baseMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public Integer consumeCount(Integer orderFrom, String startTime, String endTime) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_from", orderFrom);
        queryWrapper.ge("create_date", startTime);
        queryWrapper.le("create_date", endTime);
        return baseMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public Page<PointList> userPage(Long userId, String startDate, String endDate, Integer type, Integer pageNum, Integer pageSize) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        if(StringUtils.isNotBlank(startDate)){
            queryWrapper.ge("create_date", startDate);
        }
        if(StringUtils.isNotBlank(endDate)){
            queryWrapper.le("create_date", endDate);
        }
        if(type != null){
            queryWrapper.eq("order_from", type);
        }
        queryWrapper.select("order_from_chn","create_date","point");
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Integer getCountByUserIdAndType(Long userId, Integer type) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("order_from", type);
        return baseMapper.selectCount(queryWrapper).intValue();
    }


    @Override
    public BigDecimal sumByInvitation(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<PointList>().select("sum(point) as point")
                .eq("invite_user_id", userId)).getPoint();
    }

    @Override
    public BigDecimal invitationReward(Long userId) {
        int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "邀请", "5"));
        return baseMapper.selectOne(new QueryWrapper<PointList>().select("sum(point) as point")
                .eq("user_id", userId).eq("order_from",from)).getPoint();
    }
}
