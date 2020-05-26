package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;


public interface OrderCountMapper extends BaseMapper<T> {

    List<Map<String, Object>>getOrderCountList(@Param("companyId") Long companyId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<Map<String, Object>>getOrderCountList1();

    Integer getOrderCountList2();
}