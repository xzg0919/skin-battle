package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.PointList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PointListMapper extends BaseMapper<PointList>{

    /**
     * 更新积分、积分流水、订单的新增aliserId 和 积分流水的 card_no 为 虚拟卡
     * @param aliUserId
     * @param mobile
     */
    void updatePointAndOrderFromDsdd(@Param("aliUserId") String aliUserId,@Param("mobile") String mobile, @Param("cardNo") String cardNo);

    List<Map<String, Object>> getPointInfoEndWithCreateDate(@Param("endDate") String endDate );

    List<Map<String, Object>> getReducePointInfoEndWithCreateDate();
}
