package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;


public interface OrderCountMapper extends BaseMapper<T> {

    List<Map<String, Object>>getOrderCountList();

    List<Map<String, Object>>getOrderCountList1();

    Integer getOrderCountList2();
}