package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.PointInfo;
import com.skin.entity.PointList;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 16:55
 * @Description:
 */
public interface PointListService extends IService<PointList> {


    Page<Map<String, Objects>> getPointListPage(Integer pageNum, Integer pageSize, Long userId,Integer orderFrom,String orderNo);


    BigDecimal sumPoint(Integer orderFrom);

    BigDecimal sumPoint(Integer orderFrom,String startTime,String endTime);

    Integer consumeCount(Integer orderFrom);

    Integer consumeCount(Integer orderFrom,String startTime,String endTime);


    Page<PointList> userPage(Long userId,String startDate,String endDate,Integer type,Integer pageNum,Integer pageSize);


    Integer getCountByUserIdAndType(Long userId,Integer type);


    BigDecimal invitationReward(Long userId);
}
